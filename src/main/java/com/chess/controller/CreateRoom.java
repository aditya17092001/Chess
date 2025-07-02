package com.chess.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chess.constants.RestURLs;
import com.chess.dto.GameCreateDto;
import com.chess.service.JwtService;
import com.chess.service.RoomIdGeneratorService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping(RestURLs.V1_API)
public class CreateRoom {
	
	@Autowired
	private RoomIdGeneratorService roomIdGeneratorService;
	
	@Autowired
	private JwtService jwtService;
	
	@GetMapping(RestURLs.GAME_CREATE)
	public ResponseEntity<?> createGame() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String jwt = SecurityContextHolder.getContext().getAuthentication().toString();
		System.out.println(username);
		System.out.println(jwt);

	    Claims claims = jwtService.extractAllClaimsFromContext();
	    String userId = claims.getSubject();
	    
	    String roomId = roomIdGeneratorService.generateUniqueId();

	    HashMap<String, String> response = new HashMap<>();
	    response.put("roomId", roomId);
	    return ResponseEntity.ok(response); 
	}
}
