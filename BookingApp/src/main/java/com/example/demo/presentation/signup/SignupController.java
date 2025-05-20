package com.example.demo.presentation.signup;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.application.service.signup.SignupAppService;
import com.example.demo.domain.model.Member;

import lombok.RequiredArgsConstructor;

/*
 * 会員登録 Controller
 */
@RequiredArgsConstructor
@Controller
public class SignupController {

	private final SignupAppService signupAppService;
	private final SignupFormValidator signupFormValidator;
	private final ModelMapper modelMapper;
	
	@InitBinder("signupForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(signupFormValidator);
	}
	
	@ModelAttribute
	public SignupForm setUpSignupForm() {
		return new SignupForm();
	}
	
	/**
	 * 会員登録フォーム表示
	 * @return
	 */
	@GetMapping("/signup")
	public String signupForm() {
		return "signup/signup";
	}
	
	/**
	 * 会員登録
	 * @param signupForm 会員情報
	 * @param result
	 * @return
	 */
	@PostMapping("/signup")
	public String createMember(@Validated SignupForm signupForm, BindingResult result) {
		
		if (result.hasErrors()) {
			return "signup/signup";
		}
		
		Member member = modelMapper.map(signupForm, Member.class);
		
		signupAppService.createMember(member);
		
		return "redirect:/";
	}
}
