package com.chess.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chess.constants.RestURLs;
import com.chess.dto.AuthRequest;
import com.chess.model.Users;
import com.chess.repo.UserRepo;
import com.chess.service.UserService;

@RestController
@RequestMapping(RestURLs.V1_API)
public class Authentication {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private UserService service;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome";
	}
	
	@PostMapping(RestURLs.SIGNUP) 
	public ResponseEntity<?> signup(@RequestBody AuthRequest req) {
		Users user = repo.findByUserName(req.getUserName());

		HashMap<String, String> response = new HashMap<>();
		
		if(user != null) {
			response.put("message", "Username already exists.");
			return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
		}
		
		Users newUser = new Users();
		newUser.setUserName(req.getUserName());
		newUser.setPassword(bCryptPasswordEncoder.encode(req.getPassword())); 
		newUser.setWins(0);
		newUser.setStatus("active");
		
		repo.save(newUser);
				
		String userJwt = service.verify(req);
		
		if(userJwt.equals("")) {
			response.put("message", "Wrong Credentials");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(response);
		}
		
		
	    response.put("token", userJwt);

	    return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(response);
	}
	
	@PostMapping(RestURLs.SIGNIN)
	public ResponseEntity<?> signin(@RequestBody AuthRequest req) {
		String userJwt = service.verify(req);
		
		HashMap<String, String> response = new HashMap<>();
		if(userJwt.equals("")) {
			response.put("message", "Wrong Credentials");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(response);
		}
		
	    response.put("token", userJwt);

	    return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(response);
	}
}
