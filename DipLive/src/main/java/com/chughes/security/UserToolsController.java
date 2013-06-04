package com.chughes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserToolsController {

	@Autowired
    UserDAO us;
	
	@RequestMapping(value="/newuser")
	public String newUser(Model model){
		UserDetailsImpl user = new UserDetailsImpl();
		model.addAttribute("user", user);
		return "newuser";
	}

	@RequestMapping(value="/saveuser")
	public String saveUser(@ModelAttribute("user") UserDetailsImpl user){

		us.saveUser(user);
		return "newuser";
	}
}
