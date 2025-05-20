package com.example.demo.presentation.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
 * メールアドレス変更フォーム
 */
@Data
public class UpdateMailForm {

	/** メールアドレス */
	@NotBlank
	@Email
	private String mail;
}
