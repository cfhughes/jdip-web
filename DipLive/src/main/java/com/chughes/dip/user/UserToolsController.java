package com.chughes.dip.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value="/account")
	public String account(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
			UserEntity ue = us.getUserEntity(user.getId());
			model.addAttribute("user", ue);
		}
		return "account";
	}

	@Autowired private BCryptPasswordEncoder encoder;

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value="/updateuser")
	public String updateUser(@Valid UserDetailsImpl user,@RequestParam(value="pass")String pass) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			if (us.getUserByName(user.getUsername()) != null){
				throw new Exception("Username Taken");
			}
			UserDetailsImpl user1 = (UserDetailsImpl) auth.getPrincipal();
			UserEntity usernew = us.getUserEntity(user1.getId());
			usernew.setUsername(user.getUsername());
			usernew.setEmail(user.getEmail());

			if (pass.length() < 5 && pass.length() > 0){
				throw new Exception("Password must be at least 5 characters");
			}else if (pass.length() >= 5){
				usernew.setPassword(encoder.encode(pass));
			}		

			us.editUser(usernew);
			
		}
		return "updateuser";
	}

	@RequestMapping(value="/privacy")
	public String privacy(){

		return "privacy";
	}
}
