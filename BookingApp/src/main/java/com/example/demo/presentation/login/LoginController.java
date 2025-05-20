package com.example.demo.presentation.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * ログイン Controller
 */
@Controller
public class LoginController {
	
	/**
	 * ログイン
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
}
