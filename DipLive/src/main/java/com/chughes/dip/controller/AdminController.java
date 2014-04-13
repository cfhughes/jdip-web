package com.chughes.dip.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

	@PreAuthorize("hasRole('MODERATOR')")
	@RequestMapping("/admin")
	public String admin(){
		return "admin";
	}
	
	@PreAuthorize("hasRole('MODERATOR')")
	@RequestMapping("/admin/draw")
	public String draw(@RequestParam(value="id")Integer id){
		
		return "admin";
	}
}
