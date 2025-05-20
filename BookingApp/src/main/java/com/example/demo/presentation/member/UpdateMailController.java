package com.example.demo.presentation.member;

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

import com.example.demo.application.service.member.UpdateMailAppService;
import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * メールアドレス変更 Controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class UpdateMailController {

	private final UpdateMailAppService updateMailAppService;
	private final UpdateMailFormValidator updateMailFormValidator;
	
	@InitBinder("updateMailForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(updateMailFormValidator);
	}
	
	@ModelAttribute
	public UpdateMailForm setUpUpdateMailForm() {
		return new UpdateMailForm();
	}
	
	/**
	 * メールアドレス変更フォーム表示
	 * @param authenticatedMember 認証済み会員
	 * @param model
	 * @return
	 */
	@GetMapping("/mail")
	public String form(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, Model model) {
		
		model.addAttribute("currentMail", authenticatedMember.getMail());
		
		return "member/mail/mail";
	}
	
	/**
	 * メールアドレス変更
	 * @param authenticatedMember 認証済み会員
	 * @param updateMailForm 変更後メールアドレス情報
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("/mail")
	public String update(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, @Validated UpdateMailForm updateMailForm, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return form(authenticatedMember, model);
		}
		
		// メールアドレス変更
		updateMailAppService.update(authenticatedMember.getId(), updateMailForm.getMail());
		
		return "redirect:/member/mail?complete";
	}
	
	/**
	 * メールアドレス変更完了画面表示
	 * @return
	 */
	@GetMapping(value = "/mail", params = "complete")
	public String complete() {
		return "member/mail/updateComplete";
	}
}
