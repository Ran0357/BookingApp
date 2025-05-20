package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			// 認証リクエストの設定
			.authorizeHttpRequests(auth -> auth
					// cssやjsなどの静的リソースをアクセス可能にする
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.requestMatchers("/","/login", "/logout").permitAll()
					.requestMatchers("/signup/**").permitAll()
					.requestMatchers("/school/member/**").authenticated()
					.requestMatchers("/school/**").permitAll()
					.requestMatchers("/api/**").permitAll()
					// 認証の必要があるように設定
					.anyRequest().authenticated())
			.formLogin(login -> login
					.loginProcessingUrl("/login")
					.loginPage("/login")
					.usernameParameter("mail")
					.passwordParameter("password")
					.defaultSuccessUrl("/", false))
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")
					.deleteCookies("JSESSIONID"))
		    .csrf(csrf -> csrf
					.ignoringRequestMatchers("/api/**") );
		
		return http.build();
	}
}
