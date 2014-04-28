package com.chughes.dip.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	public UserDetailsImpl getUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (UserDetailsImpl)auth.getPrincipal();
	}
	
	public void updateLevel(UserEntity ue){
		if (ue.getLosses() + ue.getWins() > 10 && ue.getScore() > 75){
			ue.setLevel(ue.getLevel() + 1);
		}
	}
}
