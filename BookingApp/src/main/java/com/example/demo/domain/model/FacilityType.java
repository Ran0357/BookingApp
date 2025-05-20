package com.example.demo.domain.model;

import lombok.Data;

/*
 * サイトタイプ
 */
@Data
public class FacilityType {

	/** サイトタイプID */
	private int id;
	
	/** サイトタイプ名 */
	private String name;
	
	/** 収容人数 */
	private int capacity;
	
}
