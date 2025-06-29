package com.chess.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
	
	public static final String KEY = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

	public String generateToken(UUID uuid, String userName) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userName", userName);
		
		return Jwts
				.builder()
				.claims() 
				.add(claims)
				.subject(new String(uuid+""))
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				.and()
				.signWith(getKey())
				.compact();
				
	}
	
	private Key getKey() {
		byte keyByte[] = Decoders.BASE64.decode(KEY);
		return Keys.hmacShaKeyFor(keyByte);
	}

	public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userName", String.class);
    }

    public UUID extractId(String token) {
        Claims claims = extractAllClaims(token);
        String subject = claims.getSubject();
        return UUID.fromString(subject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = extractAllClaims(token);
            String username = extractUserName(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(claims));
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
