package com.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chess.model.UserPrincipal;
import com.chess.model.Users;
import com.chess.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired 
	private UserRepo repo ;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = repo.findByUserName(username);
		
		if(user == null) {
			System.out.println("User Not found");
			throw new UsernameNotFoundException("User Not found");
		}
		System.out.println("User found");
		return new UserPrincipal(user);
	}
}
