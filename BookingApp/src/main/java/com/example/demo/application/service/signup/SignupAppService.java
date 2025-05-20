package com.example.demo.application.service.signup;

import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.service.MemberService;

import lombok.RequiredArgsConstructor;

/*
 * 会員登録 Application Service
 */
@RequiredArgsConstructor
@Service
public class SignupAppService {

	private final MemberService memberService;
	
	/**
	 * 会員登録
	 * @param member 会員情報
	 */
	public void createMember(Member member) {
		memberService.createMember(member);
	}
}
