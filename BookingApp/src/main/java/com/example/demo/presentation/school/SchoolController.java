package com.example.demo.presentation.school;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.application.service.GymEquipmentService;
import com.example.demo.application.service.school.ReserveAppService;
import com.example.demo.application.service.school.SchoolAppService;
import com.example.demo.domain.model.FacilityReservationInfo;
import com.example.demo.domain.model.GymEquipment;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.Reservations;
import com.example.demo.domain.model.UserInfo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.SystemException;
import com.example.demo.security.AuthenticatedMember;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/*
 * 学校施設情報表示・予約 Controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/school")
public class SchoolController {
	
	private final SchoolAppService schoolAppService;
	private final ReserveAppService reserveAppService;
	private final GymEquipmentService gymEquipmentService;  // 追加
	private final FacilityUseFormValidator facilityUseFormValidator;
	private final HttpSession session;
	private final ModelMapper modelMapper;
	private final MessageSource messageSource;
	
	@InitBinder("facilityUseForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(facilityUseFormValidator);
	}
	
	@ModelAttribute
	public FacilityUseForm setUpFacilityUseForm() {
		return new FacilityUseForm();
	}
	
	@ModelAttribute
	public UserInfoForm setUpUserInfoForm() {
		return new UserInfoForm();
	}
	
	/**
	 * 施設タイプ一覧表示
	 */
	@GetMapping("/facilityTypes")
	public String facilityTypeList(Model model) {
		model.addAttribute("facilityTypeList", schoolAppService.findAllFacilityType());
		return "school/facilityTypes";
	}
	
	/**
	 * スケジュール表示
	 */
	@GetMapping(value = "/schedule", params = "facilityTypeId")
	public String schedule(@RequestParam("facilityTypeId") int facilityTypeId, Model model) {
	    model.addAttribute("facilityTypeId", facilityTypeId);
	    model.addAttribute("facilityTypeName", schoolAppService.findFacilityTypeName(facilityTypeId));

	    if (facilityTypeId == 2) {
	        // 利用状況も取得するように修正
	        model.addAttribute("gymEquipmentList", gymEquipmentService.findCurrentStatus());
	        return "school/gym";
	    }

	    return "school/schedule";
	}

	/**
	 * ジム機器利用状況表示
	 */
	@GetMapping("/gym-status")
	public String showGymStatus(Model model) {
		// 利用状況を含むリスト取得
		var list = gymEquipmentService.findCurrentStatus();
		model.addAttribute("gymEquipmentList", list);
		return "school/gym";  // gym-status.html に対応
	}
	
	@GetMapping("/gym/equipment/toggle")
	public String toggleEquipmentUseStatus(@RequestParam("id") Integer equipmentId, Model model) {
	    // 該当機器の情報取得
	    GymEquipment equipment = gymEquipmentService.findById(equipmentId);
	    if (equipment == null) {
	        model.addAttribute("message", "該当する機器が見つかりません。");
	        return "gym/toggleResult";
	    }

	    // 利用状況を反転して更新
	    boolean newStatus = !equipment.isInUse();
	    boolean updated = gymEquipmentService.updateUseStatus(equipmentId, newStatus);

	    if (!updated) {
	        model.addAttribute("message", "状態の更新に失敗しました。");
	        return "gym/toggleResult";
	    }

	    // メッセージセット（使用中になったかどうかで文言変える）
	    String msg = newStatus ? "使用中になりました。" : "お疲れさまでした。";
	    model.addAttribute("message", msg);
	    model.addAttribute("equipmentName", equipment.getEquipmentName());

	    return "school/toggleResult"; // 切り替え結果表示用テンプレート
	}

	
	/**
	 * 施設情報画面
	 */
	@GetMapping("/facilityInfo")
	public String facilityInfo(
	    @RequestParam("facilityId") int facilityTypeId,
	    @RequestParam("useDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate useDate,
	    FacilityUseForm facilityUseForm
	) {
	    // フォームに値セット
	    facilityUseForm.setFacilityId(facilityTypeId);
	    facilityUseForm.setUseDate(useDate);

	    String facilityTypeName = schoolAppService.findFacilityTypeName(facilityTypeId);
	    facilityUseForm.setFacilityName(facilityTypeName);

	    return "school/facilityInfo";
	}

	/**
	 * 施設情報 → 予約内容確認（会員）
	 */
	@PostMapping(value = "/facilityInfo", params = "member")
	public String sendToReserve(@ModelAttribute FacilityUseForm facilityUseForm, BindingResult result) {
		if (facilityUseForm.getFacilityId() == 1) {
	        facilityUseForm.setStartTime(LocalTime.of(15, 0));
	        facilityUseForm.setEndTime(LocalTime.of(17, 30));
	    }

	    facilityUseFormValidator.validate(facilityUseForm, result);
		if (result.hasErrors()) {
			return "school/facilityInfo";
		}
		session.setAttribute("facilityUseFormSession", facilityUseForm);
		return "redirect:/school/member/reserve?confirm";
	}
	
	/**
	 * 予約内容確認（会員）
	 */
	@GetMapping(value = "/member/reserve", params = "confirm")
	public String confirmByMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, Model model) {
		FacilityUseForm facilityUseForm = (FacilityUseForm) session.getAttribute("facilityUseFormSession");
		FacilityReservationInfo facilityInfo = modelMapper.map(facilityUseForm, FacilityReservationInfo.class);

		Member member = reserveAppService.findMemberById(authenticatedMember.getId());
		UserInfo userInfo = new UserInfo();
		userInfo.setId(member.getId());
		Reservations reservation = reserveAppService.buildReservation(facilityInfo, userInfo);

		UserInfoForm userInfoForm = modelMapper.map(member, UserInfoForm.class);

		model.addAttribute("reservation", reservation);
		model.addAttribute("userInfoForm", userInfoForm);
		model.addAttribute("facilityUseForm", facilityUseForm);
		
		return "school/confirm";
	}
	
	/**
	 * 予約（会員）
	 */
	@PostMapping("/member/reserve")
	public String reserveByMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember,
								  @Validated FacilityUseForm facilityUseForm, BindingResult result) {

		if (result.hasErrors()) {
			boolean outOfStockFlg = result.getFieldErrors()
				.stream()
				.anyMatch(e -> e.getCode().equals("validation.custom.siteIsNotAvailable"));

			if (outOfStockFlg) {
				throw new BusinessException(messageSource.getMessage("exception.siteIsNotAvailable", null, Locale.JAPAN));
			}
			throw new SystemException(messageSource.getMessage("exception.errorAtCreate", null, Locale.JAPAN));
		}

		FacilityReservationInfo facilityInfo = modelMapper.map(facilityUseForm, FacilityReservationInfo.class);

		UserInfo userInfo = new UserInfo();
		userInfo.setId(authenticatedMember.getId());

		Reservations reservation = reserveAppService.buildReservation(facilityInfo, userInfo);
		reserveAppService.saveReservation(reservation);

		return "redirect:/school/member/reserve?complete";
	}
	
	/**
	 * 予約完了画面（会員）
	 */
	@GetMapping(value = "/member/reserve", params = "complete")
	public String completeByMember() {
		return "school/complete";
	}
	
	/**
	 */
	    @GetMapping("/guide")
	    public String showFacilityGuidePage() {
	        return "school/guide"; 
	    }

	
}
