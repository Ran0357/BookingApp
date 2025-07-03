package com.example.demo.presentation.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String login(@RequestParam(value = "loginRequired", required = false) String loginRequired,
            Model model) {
		if (loginRequired != null) {
	        model.addAttribute("message", "ログインしてください");
	    }
	    return "login/login";  
	}
		
}
