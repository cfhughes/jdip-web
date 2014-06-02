package com.chughes.dip.user;

import org.springframework.http.converter.HttpMessageNotReadableException;
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

		String newname = "";
		newname = profile.getUsername();
		//newguy.setEmail(profile.getEmail());
		System.out.println(conn.getApi().getClass());
		if (conn.getApi() instanceof Google){
			//System.out.println("in Google");
			Google google = (Google) conn.getApi();
			try{
				Person guser = google.plusOperations().getGoogleProfile();
				newname = guser.getDisplayName();
			}catch(HttpMessageNotReadableException e){
				e.printStackTrace();
			}
			//System.out.println("fn: "+guser.getDisplayName());

		}
		newguy.setUsername(newname);
		for (int i = 1;i < 100;i++){
			if (userRepo.getUserByName(newguy.getUsername()) != null){
				newguy.setUsername(newname + "" + i);
			}else{
				break;
			}
		}
		userRepo.saveUser(newguy);
		return newguy.getId()+"";
	}

}
