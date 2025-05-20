package com.example.demo.presentation.member;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * パスワード変更フォーム Validator
 */
@RequiredArgsConstructor
@Component
public class UpdatePasswordFormValidator implements Validator {
	
	private final PasswordEncoder passwordEncoder;

	/**
	 * チェック対象となるクラスがUpdatePasswordFormクラスかチェックする
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return UpdatePasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		UpdatePasswordForm updatePasswordForm = (UpdatePasswordForm) target;
		
		// 現在のパスワード一致検証
		validateIsDifferentFormCurrentPassword(errors, updatePasswordForm.getCurrentPassword());
	}
	
	/**
	 * 現在パスワード一致検証
	 * 入力された現在のパスワードが登録内容と一致しない場合エラー
	 * @param errors
	 * @param currentPassword 現在のパスワード
	 */
	private void validateIsDifferentFormCurrentPassword(Errors errors, String currentPassword) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (!passwordEncoder.matches(currentPassword, ((AuthenticatedMember) auth.getPrincipal()).getPassword())) {
			errors.rejectValue("currentPassword", "validation.custom.currentPasswordIncorrect");
		}
	}
}
