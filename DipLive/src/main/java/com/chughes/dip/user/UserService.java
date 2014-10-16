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
		if (ue.getRoundgamesplayed() > 5 && ue.getScore() > 75){
			ue.setLevel(ue.getLevel() + 1);
		}else if (ue.getRoundgamesplayed() > 5 && ue.getScore() < 10){
			ue.setLevel(ue.getLevel() - 1);
		}
		ue.setScore(50);
		ue.setRoundgamesplayed(0);
	}
}
