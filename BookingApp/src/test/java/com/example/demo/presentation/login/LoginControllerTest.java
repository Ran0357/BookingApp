package com.example.demo.presentation.login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/*
 * ログイン Controller Test
 */
@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("正常系：ログイン画面を表示する")
	void testLogin() throws Exception {
		
		mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login/login"));
	}

}
