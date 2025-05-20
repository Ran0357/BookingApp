package com.example.demo.presentation.member;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/*
 * パスワード変更フォーム
 */
@Data
public class UpdatePasswordForm {

	/** 現在のパスワード */
	@NotBlank
	@Length(min = 8, message = "8文字以上で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "半角英数字で入力してください")
	private String currentPassword;
	
	/** 新しいパスワード */
	@NotBlank
	@Length(min = 8, message = "8文字以上で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "半角英数字で入力してください")
	private String password;
}
