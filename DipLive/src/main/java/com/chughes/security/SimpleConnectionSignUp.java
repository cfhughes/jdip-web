package com.chughes.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;


public class SimpleConnectionSignUp implements ConnectionSignUp {
	
	private UserDAO userRepo;

	public SimpleConnectionSignUp(UserDAO user) {
		userRepo = user;
	}

	@Override
	public String execute(Connection<?> conn) {
		UserProfile profile = conn.fetchUserProfile();
		UserEntity newguy = new UserEntity();
		newguy.setUsername(profile.getFirstName());
		userRepo.saveUser(newguy);
		return newguy.getId()+"";
	}

}
