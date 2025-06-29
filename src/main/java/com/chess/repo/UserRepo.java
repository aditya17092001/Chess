package com.chess.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chess.model.Users;

public interface UserRepo extends JpaRepository<Users, UUID> {
	 Users findByUserName(String userName);
}

