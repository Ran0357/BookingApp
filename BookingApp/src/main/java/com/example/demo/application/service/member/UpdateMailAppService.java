package com.example.demo.application.service.member;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.service.MemberService;
import com.example.demo.exception.SystemException;
import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * メールアドレス変更 Application Service
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UpdateMailAppService {

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
	 * メールアドレス変更
	 * @param memberId 会員ID
	 * @param newMail 更新後メールアドレス
	 */
	public void update(int memberId, String newMail) {
		
		Member member = findMember(memberId);
		member.setMail(newMail);
		
		// メールアドレス更新
		memberService.updateMail(member);
		this.updatePrincipalOfSecurityContext(member);
	}
	
	/**
	 * 認証情報更新
	 * @param updatedMember 更新後会員情報
	 */
	private void updatePrincipalOfSecurityContext(Member updatedMember) {
		
		AuthenticatedMember newAuthenticatedMember = new AuthenticatedMember(updatedMember);
		// 現在認証されている情報を取得する
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 変更後認証情報生成
		Authentication newAuth = new UsernamePasswordAuthenticationToken(newAuthenticatedMember, auth.getCredentials(), auth.getAuthorities());
		// 変更後認証情報設定
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
}
