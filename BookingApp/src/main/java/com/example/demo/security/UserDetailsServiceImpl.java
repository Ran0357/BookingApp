package com.example.demo.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Member;
import com.example.demo.repository.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		Optional<Member> loginMember = memberMapper.findByMail(mail);
		
		return loginMember.map(member -> new AuthenticatedMember(member)).orElseThrow(() -> new UsernameNotFoundException(mail + " not found"));
	}

}
