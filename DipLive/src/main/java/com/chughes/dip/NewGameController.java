package com.chughes.dip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chughes.dip.GameEntity.Stage;
import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;
import com.chughes.security.UserEntity;
import com.chughes.service.GameService;

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
    UserDAO us;
	
	@RequestMapping(value="/newgame")
	public String newGame(Model model){
		GameEntity ge = new GameEntity();
		model.addAttribute("game", ge);
		model.addAttribute("variants",VariantManager.getVariants());
		return "newgame";
	}
	
	@RequestMapping(value = "/savegame")
	public String saveGame(Model model,@ModelAttribute("game")GameEntity game,@RequestParam(value="variant")String variant){

		Variant vs = VariantManager.getVariant(variant, 1.0f);
		World w = null;
		try {
			w = WorldFactory.getInstance().createWorld(vs);
		} catch (InvalidWorldException e) {
			e.printStackTrace();
		}
		VariantInfo vi1 =new VariantInfo();
		vi1.setVariantName(variant);
		vi1.setVariantVersion(1.0f);
		w.setVariantInfo(vi1);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user1 = (UserDetails)auth.getPrincipal();
		UserDetailsImpl user = (UserDetailsImpl) user1;
		
		UserEntity ue = us.getUserEntity(user.getId());
		
		game.setW(w);
		game.setStage(Stage.PREGAME);
		game.setMaxplayers(vs.getPowers().length);
		
		gameService.saveGame(game);
		gameService.addUserToGame(game, ue);

		model.addAttribute("id", game.getId());
		
		return "savegame";

	}
	
}
