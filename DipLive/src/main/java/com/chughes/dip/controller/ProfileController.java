package com.chughes.dip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chughes.dip.data.UserRepository;
import com.chughes.dip.user.UserEntity;

@Controller
public class ProfileController {
	
	@Autowired UserRepository us;
	
	@RequestMapping(value="/player/{id}")
	public String profile(@PathVariable(value="id") int id, Model m){
		UserEntity user = us.getUserEntity(id);
		m.addAttribute("user", user);
		return "profile";
	}

}
