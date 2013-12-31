package com.chughes.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserToolsController {

	@Autowired
    UserDAO us;
	
	@RequestMapping(value="/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping(value="/signin")
	public String signin(){
		return "redirect:/login";
	}
	
	@RequestMapping(value="/newuser")
	public String newUser(Model model){
		UserDetailsImpl user = new UserDetailsImpl();
		model.addAttribute("user", user);
		return "newuser";
	}
	
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}

	@RequestMapping(value="/saveuser")
	public String saveUser(@Valid UserDetailsImpl user) throws Exception{

		us.saveUser(user);
		return "saveuser";
	}
	
	@RequestMapping(value="/not-found")
	public String notfound(){
		return "not-found";
	}
	
	@RequestMapping(value="/account")
	public String account(){
		
		return "account";
	}
	
	@RequestMapping(value="/privacy")
	public String privacy(){
		
		return "privacy";
	}
}
