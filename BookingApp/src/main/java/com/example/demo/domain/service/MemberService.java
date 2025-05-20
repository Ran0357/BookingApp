package com.example.demo.domain.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.Member;
import com.example.demo.repository.MemberMapper;
import com.example.demo.security.Role;

import lombok.RequiredArgsConstructor;

/*
 * 会員 Domain Service
 */
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * 会員情報取得
	 * @param mail メールアドレス
	 * @return
	 */
	public Optional<Member> findByMail(String mail) {
		return memberMapper.findByMail(mail);
	}
	
	/**
	 * 会員情報取得
	 * @param memberId 会員ID
	 * @return
	 */
	public Optional<Member> findById(int memberId) {
		return memberMapper.findById(memberId);
	}
	
	/**
	 * 会員登録
	 * @param member 会員情報
	 */
	public void createMember(Member member) {
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		member.setRole(Role.ROLE_GENERAL.toString());
		memberMapper.createMember(member);
	}
	
	/**
	 * 会員基本情報更新
	 * @param member 更新後会員情報
	 */
	public void updateProfile(Member member) {
		memberMapper.updateProfile(member);
	}
	
	/**
	 * メールアドレス変更
	 * @param member 更新後メールアドレス
	 */
	public void updateMail(Member member) {
		memberMapper.updateMail(member);
	}
	
	/**
	 * パスワード変更
	 * @param memberId 会員ID
	 * @param newPassword 暗号化前パスワード
	 */
	public void updatePassword(int memberId, String newPassword) {
		
		String encodePassword = passwordEncoder.encode(newPassword);
		
		memberMapper.updatePassword(memberId, encodePassword);
	}
}
