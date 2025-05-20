package com.example.demo.presentation.member;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.application.service.member.ReservationsAppService;
import com.example.demo.domain.model.Reservations;
import com.example.demo.exception.SystemException;
import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * 予約内容確認・キャンセル Controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/member/reservations")
public class ReservationsController {

	private final ReservationsAppService reservationsAppService;
	private final MessageSource messageSource;
	
	/**
	 * 予約一覧
	 * @param authenticatedMember 認証済み会員情報
	 * @param pageable
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, @PageableDefault(size = 5) Pageable pageable, Model model) {
		
		// 予約検索(ページネーション)
		Page<Reservations> page = reservationsAppService.searchReservations(authenticatedMember.getId(), pageable);
		
		model.addAttribute("page", page);
		
		return "member/reservations/list";
	}
	
	/**
	 * 予約詳細
	 * @param reservationId 予約ID
	 * @param pageNumber 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/{reservationId}", params = "page")
	public String detail(@PathVariable int reservationId, @RequestParam("page") int pageNumber, Model model) {
		
		model.addAttribute("reservation", reservationsAppService.findReservationDetailsById(reservationId));
		model.addAttribute("pageNumber", pageNumber);
		
		return "member/reservations/detail";
	}
	
	/**
	 * 予約キャンセル
	 * @param reservationId 予約ID
	 * @return
	 */
	@PostMapping("/{reservationId}/cancel")
	public String cancel(@PathVariable int reservationId) {
		
		// 予約キャンセル
		int canceledCount = reservationsAppService.cancelReservation(reservationId);
		
		if (canceledCount == 0) {
			throw new SystemException(messageSource.getMessage("exception.errorAtCancel", null, Locale.JAPAN));
		}
		
		return "member/reservations/complete";
	}
}
