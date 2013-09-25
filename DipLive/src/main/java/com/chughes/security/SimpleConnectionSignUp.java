package com.chughes.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;


public class SimpleConnectionSignUp implements ConnectionSignUp {
	
	private UserDAO userRepo;

	public SimpleConnectionSignUp(UserDAO user) {
		userRepo = user;
	}

	@Override
	public String execute(Connection<?> conn) {
		UserEntity newguy = new UserEntity();
		newguy.setUsername(conn.getDisplayName());
		userRepo.saveUser(newguy);
		return newguy.getId()+"";
	}

}
