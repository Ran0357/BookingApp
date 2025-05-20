package com.example.demo.presentation.school;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/*
 * 予約者情報フォーム
 */
@Data
public class UserInfoForm {
	
	/** 名前 */
	@NotBlank
	@Length(max = 100, message = "100文字以内で入力してください")
	private String name;

	/** メールアドレス */
	@NotBlank
	@Email
	private String mail;
	
	/** 電話番号 */
	@NotBlank
	@Length(max = 15, message = "15桁以内で入力してください")
	@Pattern(regexp = "^[0-9]+$", message = "半角数字で入力してください")
	private String phoneNumber;
}
