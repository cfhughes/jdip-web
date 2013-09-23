package com.chughes.dip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;
import com.chughes.security.UserEntity;
import com.chughes.service.GameService;

@Controller
public class GameListController {

	@Autowired private GameService gameService;
	@Autowired private UserDAO userrepo;

	@RequestMapping(value="/gamelist")
	public String listGames(Model model){
		
		model.addAttribute("games", gameService.searchGames());
		
		return "gamelist";
	}
	
	@RequestMapping(value="/joingame/{gameID}")
	public String join(Model model,@PathVariable(value="gameID") int id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetailsImpl user = (UserDetailsImpl)auth.getPrincipal();
			UserEntity ue = userrepo.getUserEntity(user.getId());
			GameEntity ge = gameService.getGame(id);
			gameService.addUserToGame(ge, ue);
		}
		return "redirect:../game/"+id;
	}
}
