package com.example.demo.presentation.member;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.application.service.member.UpdateProfileAppService;
import com.example.demo.domain.model.Member;
import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * 会員基本情報更新 Controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class UpdateProfileController {

	private final UpdateProfileAppService updateProfileAppService;
	private final ModelMapper modelMapper;
	
	@ModelAttribute
	public UpdateProfileForm setUpUpdateProfileForm() {
		return new UpdateProfileForm();
	}
	
	/**
	 * 会員基本情報更新フォーム表示
	 * @param authenticatedMember 認証済み会員
	 * @param updateProfileForm 更新後会員基本情報
	 * @return
	 */
	@GetMapping("/profile")
	public String form(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, UpdateProfileForm updateProfileForm) {
		
		// 会員情報取得
		Member member = updateProfileAppService.findMember(authenticatedMember.getId());
		
		modelMapper.map(member, updateProfileForm);
		
		return "member/profile/profile";
	}
	
	/**
	 * 会員基本情報更新
	 * @param authenticatedMember 認証済み会員
	 * @param updateProfileForm 更新後会員基本情報
	 * @param result
	 * @return
	 */
	@PostMapping("/profile")
	public String update(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, @Validated UpdateProfileForm updateProfileForm, BindingResult result) {
		
		if (result.hasErrors()) {
			return "member/profile/profile";
		}
		
		Member newMember = modelMapper.map(updateProfileForm, Member.class);
		
		// 会員基本情報更新
		updateProfileAppService.update(authenticatedMember.getId(), newMember);
		
		return "redirect:/member/profile?complete";
	}
	
	/**
	 * 会員基本情報更新完了画面表示
	 * @return
	 */
	@GetMapping(value = "/profile", params = "complete")
	public String complete() {
		return "member/profile/updateComplete";
	}
}
