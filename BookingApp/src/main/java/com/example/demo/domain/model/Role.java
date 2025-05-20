package com.example.demo.domain.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Role {
	/** 識別key */
    private String key;
    /** ロール名 */
    private String name;
   
    private LocalDateTime createdAt;
}
