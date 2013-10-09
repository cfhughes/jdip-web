package com.chughes.dip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chughes.security.UserDAO;
import com.chughes.security.UserEntity;

@Controller
public class ProfileController {
	
	@Autowired UserDAO us;
	
	@RequestMapping(value="/player/{id}")
	public String profile(@PathVariable(value="id") int id, Model m){
		UserEntity user = us.getUserEntity(id);
		m.addAttribute("user", user);
		return "profile";
	}

}
