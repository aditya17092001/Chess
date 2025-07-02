package com.chess.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RoomIdGeneratorService {
	public String generateUniqueId() {
        Random random = new Random();
        int id = 100000 + random.nextInt(900000); 
        return String.format("%06d", id);
    }
}
