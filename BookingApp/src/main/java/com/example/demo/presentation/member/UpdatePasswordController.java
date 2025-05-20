package com.example.demo.presentation.member;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.application.service.member.UpdatePasswordAppService;
import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * パスワード変更 Controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class UpdatePasswordController {

	private final UpdatePasswordAppService updatePasswordAppService;
	private final UpdatePasswordFormValidator updatePasswordFormValidator;
	
	@InitBinder("updatePasswordForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(updatePasswordFormValidator);
	}
	
	@ModelAttribute
	public UpdatePasswordForm setUpUpdatePasswordForm() {
		return new UpdatePasswordForm();
	}
	
	/**
	 * パスワード変更フォーム表示
	 * @return
	 */
	@GetMapping("/password")
	public String form() {
		return "member/password/password";
	}
	
	/**
	 * パスワード変更
	 * @param authenticatedMember 認証済み会員
	 * @param updatePasswordForm 更新後パスワード情報
	 * @param result
	 * @return
	 */
	@PostMapping("/password")
	public String update(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, @Validated UpdatePasswordForm updatePasswordForm, BindingResult result) {
		
		if (result.hasErrors()) {
			return "member/password/password";
		}
		
		// パスワード変更
		updatePasswordAppService.updatePassword(authenticatedMember.getId(), updatePasswordForm.getPassword());
		
		return "redirect:/member/password?complete";
	}
	
	/**
	 * パスワード変更完了画面表示
	 * @return
	 */
	@GetMapping(value = "/password", params = "complete")
	public String complete() {
		return "member/password/updateComplete";
	}
}
