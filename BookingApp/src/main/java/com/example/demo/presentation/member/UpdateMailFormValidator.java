package com.example.demo.presentation.member;

import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.domain.service.MemberService;

import lombok.RequiredArgsConstructor;

/*
 * メールアドレス変更フォーム Validator
 */
@RequiredArgsConstructor
@Component
public class UpdateMailFormValidator implements Validator {
	
	private final MemberService memberService;
	
	/**
	 * チェック対象となるクラスがUpdateMailFormクラスかチェックする
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateMailForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		UpdateMailForm updateMailForm = (UpdateMailForm) target;
		
		// メールアドレスの重複検証
		validateDuplicateMail(errors, updateMailForm.getMail());
	}
	
	/**
	 * メールアドレスの重複検証
	 * メールアドレスが既に登録されている場合はエラー
	 * @param errors
	 * @param mail メールアドレス
	 */
	private void validateDuplicateMail(Errors errors, String mail) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		// 現在の同じメールアドレス
		if (auth.getName().toLowerCase(Locale.ROOT).equals(mail.toLowerCase(Locale.ROOT))) {
			return;
		}
		
		// メールアドレスが登録済み
		if (memberService.findByMail(mail).isPresent()) {
			errors.rejectValue("mail", "validation.custom.duplicateMail");
		}
	}
}
