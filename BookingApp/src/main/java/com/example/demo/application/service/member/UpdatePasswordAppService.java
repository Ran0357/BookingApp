package com.example.demo.application.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.MemberService;

import lombok.RequiredArgsConstructor;

/*
 * パスワード変更 Application Service
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UpdatePasswordAppService {

	private final MemberService memberService;
	
	/**
	 * パスワード変更
	 * @param memberId 会員ID
	 * @param newPassword 更新後パスワード
	 */
	public void updatePassword(int memberId, String newPassword) {
		memberService.updatePassword(memberId, newPassword);
	}
}
