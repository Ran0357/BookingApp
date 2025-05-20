package com.example.demo.presentation.member;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/*
 * 会員基本情報フォーム
 */
@Data
public class UpdateProfileForm {

	/** 名前 */
	@NotBlank
	@Length(max = 100, message = "100文字以内で入力してください")
	private String name;
	
	/** 電話番号 */
	@NotBlank
	@Length(max = 15, message = "15文字以内で入力してください")
	@Pattern(regexp = "^[0-9]+$", message = "半角数字で入力してください")
	private String phoneNumber;
}
