package com.chughes.dip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.GameEntity;
import com.chughes.dip.game.GameMaster;
import com.chughes.dip.game.GameService;
import com.chughes.dip.user.UserDetailsImpl;
import com.chughes.dip.user.UserEntity;

@Controller
public class GameListController {

	@Autowired private GameService gameService;
	@Autowired private UserRepository userrepo;
	@Autowired private GameMaster gm;


	@RequestMapping(value="/gamelist")
	public String listGames(Model model,@RequestParam(value="p", required = false) Integer p,@RequestParam(value="joinable", required = false) Integer j){
		if (p == null)p = 1;
		int offset = (p-1)*10;
		model.addAttribute("games", gameService.searchGames(offset,10,j));
		model.addAttribute("page", p);
		model.addAttribute("joinable", j);
		return "gamelist";
	}
	
	@RequestMapping(value="/joingame/{gameID}")
	public String join(Model model,@PathVariable(value="gameID") int id,@RequestParam(value="secret", required = false) String secret,@RequestParam(value="r", required = false) Integer replace) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			UserDetailsImpl user = (UserDetailsImpl)auth.getPrincipal();
			UserEntity ue = userrepo.getUserEntity(user.getId());
			GameEntity ge = gameService.getGame(id);
			if (replace != null){
				gameService.replaceUserInGame(ge, replace, ue);
			}else{
				gameService.addUserToGame(ge, ue, secret);
				if (ge.getPlayers().size() == ge.getMaxplayers()){
					gm.beginGame(ge);
				}
			}
		}
		return "redirect:../game/"+id;
	}
	
	@RequestMapping(value="/leavegame/{gameID}")
	public String leave(Model model,@PathVariable(value="gameID") int id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			UserDetailsImpl user = (UserDetailsImpl)auth.getPrincipal();
			UserEntity ue = userrepo.getUserEntity(user.getId());
			GameEntity ge = gameService.getGame(id);
			gameService.removeUserFromGame(ge, ue);
		}
		return "redirect:../game/"+id;
	}
	
}
