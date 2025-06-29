package com.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.chess.dto.AuthRequest;
import com.chess.model.Users;
import com.chess.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	public String verify(AuthRequest req) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUserName(), req.getPassword()));
		
		if(authentication.isAuthenticated()) {
			Users user = repo.findByUserName(req.getUserName());
			String jwt = jwtService.generateToken(user.getId(), req.getUserName());
			return jwt;
		}
		
		return "";
	}
}
