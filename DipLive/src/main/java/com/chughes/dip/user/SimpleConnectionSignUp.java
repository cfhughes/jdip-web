package com.chughes.dip.user;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;

import com.chughes.dip.data.UserRepository;


public class SimpleConnectionSignUp implements ConnectionSignUp {
	
	private UserRepository userRepo;

	public SimpleConnectionSignUp(UserRepository user) {
		userRepo = user;
	}

	@Override
	public String execute(Connection<?> conn) {
		UserProfile profile = conn.fetchUserProfile();
		UserEntity newguy = new UserEntity();
		newguy.setUsername(profile.getUsername());
		//newguy.setEmail(profile.getEmail());
		System.out.println(conn.getApi().getClass());
		if (conn.getApi() instanceof Google){
			//System.out.println("in Google");
			Google google = (Google) conn.getApi();
			Person guser = google.plusOperations().getGoogleProfile();
			System.out.println("fn: "+guser.getDisplayName());
			newguy.setUsername(guser.getDisplayName());
		}
		userRepo.saveUser(newguy);
		return newguy.getId()+"";
	}

}
