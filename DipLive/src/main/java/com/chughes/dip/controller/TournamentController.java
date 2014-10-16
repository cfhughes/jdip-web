package com.chughes.dip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.TournamentService;
import com.chughes.dip.user.UserDetailsImpl;
import com.chughes.dip.user.UserEntity;

@Controller
public class TournamentController {
	
	@Autowired UserRepository us;
	@Autowired TournamentService ts;
	
	@RequestMapping(value="/tournament")
	@PreAuthorize("hasRole('PLAYER')")
	public String tournament(Model m){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
		
		UserEntity ue = us.getUserEntity(user.getId());
		
		
		m.addAttribute("user",ue);
		
		return "tournament";
	}
	
	@RequestMapping(value="/tournamentjoin")
	@PreAuthorize("hasRole('PLAYER')")
	public String join(Model m) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
		
		UserEntity ue = us.getUserEntity(user.getId());
		
		int id = ts.joinTournament(ue);
		
		m.addAttribute("id",id);
		
		return "tournamentjoin";
	}
	

}
