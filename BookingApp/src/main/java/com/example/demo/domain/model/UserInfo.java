package com.example.demo.domain.model;

import lombok.Data;

/*
 * ユーザ情報
 */
@Data
public class UserInfo {

	/** ID */
	private int id;
	
	/** 名前 */
	private String name;
	
	/** メールアドレス */
	private String mail;
	
	/** 電話番号 */
	private String phone;
	
	
}
