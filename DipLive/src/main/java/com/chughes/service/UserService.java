package com.chughes.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.chughes.security.UserDetailsImpl;

@Service
public class UserService {

	public UserDetailsImpl getUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (UserDetailsImpl)auth.getPrincipal();
	}
}
