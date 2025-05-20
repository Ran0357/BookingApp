package com.example.demo.application.service.member;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.service.MemberService;
import com.example.demo.exception.SystemException;

import lombok.RequiredArgsConstructor;

/*
 * 会員メニュー Application Service
 */
@RequiredArgsConstructor
@Service
public class MenuAppService {

	private final MemberService memberService;
	private final MessageSource messageSource;
	
	/**
	 * 会員情報取得
	 * @param memberId 会員ID
	 * @return
	 */
	public Member findMember(int memberId) {
		
		return memberService.findById(memberId)
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound", new String[] {String.valueOf(memberId)}, Locale.JAPAN)));
	}
}
