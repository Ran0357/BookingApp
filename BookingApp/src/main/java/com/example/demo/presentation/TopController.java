package com.example.demo.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.application.service.TopAppService;

import lombok.RequiredArgsConstructor;

/**
 * 学校施設予約システム トップページ Controller
 */
@RequiredArgsConstructor
@Controller
public class TopController {

	private final TopAppService topAppService;
	
	/**
	 * トップページ表示
	 * 施設タイプ一覧を画面に表示
	 * 
	 * @param model Modelオブジェクト
	 * @return トップページビュー
	 */
	@GetMapping("/")
	public String top(Model model) {
		model.addAttribute("facilityTypes", topAppService.getFacilityTypeList());
		return "index";
	}
}
