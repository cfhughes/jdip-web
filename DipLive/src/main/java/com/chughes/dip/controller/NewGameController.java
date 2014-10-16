package com.chughes.dip.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.GameEntity;
import com.chughes.dip.game.GameService;
import com.chughes.dip.game.GameEntity.Stage;
import com.chughes.dip.user.UserDetailsImpl;
import com.chughes.dip.user.UserEntity;

import dip.world.InvalidWorldException;
import dip.world.World;
import dip.world.World.VariantInfo;
import dip.world.WorldFactory;
import dip.world.variant.VariantManager;
import dip.world.variant.data.Variant;

@Controller
public class NewGameController {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
    UserRepository us;
	
	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value="/newgame")
	public String newGame(Model model){
		GameEntity ge = new GameEntity();
		model.addAttribute("game", ge);
		model.addAttribute("variants",VariantManager.getVariants());
		return "newgame";
	}
	
	@RequestMapping(value="/vmap/{variant}")
	public void image(@PathVariable(value="variant") String variant,HttpServletResponse response) throws IOException{
		Variant v = VariantManager.getVariant(variant, 1.0f);
		URI mapuri = v.getDefaultMapGraphic().getURI();
		InputStream stream = VariantManager.getResource(v, mapuri).openStream();
		response.setContentType("image/svg+xml");
		IOUtils.copy(stream, response.getOutputStream());
	}
	
	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value = "/savegame")
	public String saveGame(Model model,@Valid GameEntity game,@RequestParam(value="variant")String variant) throws Exception{
		if (game.getName().contains("Tournament Game")){
			throw new Exception("You're not allowed to use the phrase 'Tournament Game'");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user1 = (UserDetails)auth.getPrincipal();
		UserDetailsImpl user = (UserDetailsImpl) user1;
		
		UserEntity ue = us.getUserEntity(user.getId());
		
		int id = gameService.newGame(variant, game);
		
		gameService.addUserToGame(game, ue, game.getSecret());

		model.addAttribute("id", id);
		
		return "savegame";

	}
	
}
