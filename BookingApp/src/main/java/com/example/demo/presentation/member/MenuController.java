package com.example.demo.presentation.member;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.application.service.member.MenuAppService;
import com.example.demo.domain.model.Member;
import com.example.demo.security.AuthenticatedMember;

import lombok.RequiredArgsConstructor;

/*
 * 会員メニュー Controller
 */
@RequiredArgsConstructor
@Controller
public class MenuController {

	private final MenuAppService menuAppService;
	
	/**
	 * 会員メニュー表示
	 * @param authenticatedMember 認証済み会員
	 * @param model
	 * @return
	 */
	@GetMapping("/member/menu")
	public String getMembersMenuPage(@AuthenticationPrincipal AuthenticatedMember authenticatedMember, Model model) {
		
		// 会員情報取得
		Member member = menuAppService.findMember(authenticatedMember.getId());
		
		model.addAttribute("userName", member.getName());
		
		return "member/menu";
	}
}
