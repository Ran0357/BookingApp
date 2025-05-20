package com.example.demo.domain.model;

import java.time.LocalDateTime;

import lombok.Data;

/*
 * 会員
 */
@Data
public class Member {

	/** ID */
	private int id;
	
	/** 名前 */
	private String name;
	
	/** メールアドレス */
	private String mail;
	
	/** パスワード */
	private String password;
	
	/** 電話番号 */
	private String phoneNumber;
	
	/** ロール */
	private String role;
	
	/** 更新日 */
	private LocalDateTime updatedAt;
	
	/** 作成日 */
	private LocalDateTime createdAt;
}