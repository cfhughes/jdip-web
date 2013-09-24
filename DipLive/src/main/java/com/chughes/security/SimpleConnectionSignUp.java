package com.chughes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

public class SimpleConnectionSignUp implements ConnectionSignUp {
	
	@Autowired private UserDAO userRepo;

	@Override
	public String execute(Connection<?> conn) {
		UserEntity newguy = new UserEntity();
		newguy.setUsername(conn.getDisplayName());
		userRepo.saveUser(newguy);
		return newguy.getId()+"";
	}

}
