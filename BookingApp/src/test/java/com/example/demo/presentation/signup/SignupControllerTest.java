package com.example.demo.presentation.signup;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.application.service.signup.SignupAppService;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.service.MemberService;

/*
 * 会員登録 Controller Test
 */
@AutoConfigureMockMvc
@SpringBootTest
class SignupControllerTest {
	
	SignupForm signupForm = new SignupForm();
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MemberService memberService;
	
	@MockBean
	private SignupAppService signupAppService;
	
	@BeforeEach
	void setUp() throws Exception {
		Member member = new Member();
		member.setMail("admin@example.com");
		
		signupForm.setMail("test@example.com");
		signupForm.setPassword("password");
		signupForm.setName("test");
		signupForm.setPhoneNumber("09012345678");
	}

	@Test
	@DisplayName("正常系：会員登録画面を表示させる")
	void testSignupForm() throws Exception {
		mockMvc.perform(get("/signup"))
		.andExpect(status().isOk())
		.andExpect(view().name("signup/signup"));
	}
	
	// 正常系
	@Test
	@DisplayName("全項目正しく入力された時、トップ画面へ遷移する")
	void testSuccess() throws Exception {
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/"))
		.andExpect(model().hasNoErrors());
	}
	
	// 異常系
	@Test
	@DisplayName("メールアドレスが未入力の時、会員登録画面に戻る")
	void testMailIsNull() throws Exception {
		signupForm.setMail(null);
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("メールアドレスの形式が不正の時、会員登録画面に戻る")
	void testMailIsInjustice() throws Exception {
		signupForm.setMail("test");
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("入力したメールアドレスが既に登録されていた時、会員登録画面に戻る")
	void testMailIsDuplication() throws Exception {
	    // 重複するメールアドレスをセット
	    signupForm.setMail("admin@example.com");

	    // メールアドレスが重複している場合、findByMailがOptional.of(member)を返すようにモック
	    Member existingMember = new Member();
	    existingMember.setMail("admin@example.com");
	    when(memberService.findByMail("admin@example.com")).thenReturn(Optional.of(existingMember));

	    // 検証 & 実行
	    mockMvc.perform(post("/signup")
	            .flashAttr("signupForm", signupForm)
	            .with(csrf()))
	        .andExpect(model().hasErrors())  // エラーがあることを確認
	        .andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("パスワードが未入力の時、会員登録画面に戻る")
	void testPasswordIsNull() throws Exception {
		signupForm.setPassword(null);
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("パスワードが7文字以下の時、会員登録画面に戻る")
	void testPasswordIsUnder7() throws Exception {
		signupForm.setPassword("testaaa");
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("名前が未入力の時、会員登録画面に戻る")
	void testNameIsNull() throws Exception {
		signupForm.setName(null);
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("名前が101文字以上の時、会員登録画面に戻る")
	void testNameIsOver101() throws Exception {
		signupForm.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("電話番号がが未入力の時、会員登録画面に戻る")
	void testPhoneNumberIsNull() throws Exception {
		signupForm.setPhoneNumber(null);
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
	
	@Test
	@DisplayName("電話番号がが16桁以上の時、会員登録画面に戻る")
	void testPhoneNumberIsOver16() throws Exception {
		signupForm.setPhoneNumber("0901234567891234");
		Member member = modelMapper.map(signupForm, Member.class);
		signupAppService.createMember(member);
		
		// 検証 & 実行
		mockMvc.perform(post("/signup")
				.flashAttr("signupForm", signupForm)
				.with(csrf()))
		.andExpect(model().hasErrors())
		.andExpect(view().name("signup/signup"));
	}
}
