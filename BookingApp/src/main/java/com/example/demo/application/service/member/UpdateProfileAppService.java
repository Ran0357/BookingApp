package com.example.demo.application.service.member;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.service.MemberService;
import com.example.demo.exception.SystemException;

import lombok.RequiredArgsConstructor;

/*
 * 会員基本情報更新 Application Service
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UpdateProfileAppService {

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
	
	/**
	 * 会員基本情報更新
	 * @param memberId 会員ID
	 * @param newMember 更新後会員情報
	 */
	public void update(int memberId, Member newMember) {
		
		Member member = findMember(memberId);
		member.setName(newMember.getName());
		member.setPhoneNumber(newMember.getPhoneNumber());
		
		// 会員基本情報更新
		memberService.updateProfile(member);
	}
}
