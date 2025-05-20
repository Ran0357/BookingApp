package com.example.demo.presentation.signup;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.domain.service.MemberService;

import lombok.RequiredArgsConstructor;

/*
 * 会員登録フォーム Validator
 */
@RequiredArgsConstructor
@Component
public class SignupFormValidator implements Validator {
	
	private final MemberService memberService;
	
	/**
	 * チェック対象となるクラスがSignupFormクラスかチェックする
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return SignupForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		SignupForm signupForm = (SignupForm) target;
		
		// メールアドレスの重複検証
		validateDuplicateMail(errors, signupForm.getMail());
	}

	/**
	 * メールアドレスの重複検証
	 * メールアドレスが既に会員登録されている場合はエラー
	 * @param errors
	 * @param mail
	 */
	private void validateDuplicateMail(Errors errors, String mail) {
		
		if (memberService.findByMail(mail).isPresent()) {
			errors.rejectValue("mail", "validation.custom.duplicateMail");
		}
	}
}
