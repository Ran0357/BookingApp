package com.example.demo.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.domain.model.Member;

/*
 * 認証会員
 */
public class AuthenticatedMember implements UserDetails {
	
	/** ID */
	private final int id;
	
	/** メールアドレス */
	private final String mail;
	
	/** パスワード */
	private final String password;
	
	/** ロール */
	private final String role;
	
	public AuthenticatedMember(Member member) {
		this.id = member.getId();
		this.mail = member.getMail();
		this.password = member.getPassword();
		this.role = member.getRole();
	}

	public int getId() {
		return id;
	}

	public String getMail() {
		return mail;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(role);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return mail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
