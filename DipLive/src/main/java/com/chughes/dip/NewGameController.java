package com.chughes.dip;

import java.util.HashSet;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;
import com.chughes.security.UserEntity;

import dip.world.InvalidWorldException;
import dip.world.World;
import dip.world.World.VariantInfo;
import dip.world.WorldFactory;
import dip.world.variant.NoVariantsException;
import dip.world.variant.VariantManager;
import dip.world.variant.data.Variant;

@Controller
public class NewGameController {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    UserDAO us;
	
	@RequestMapping(value="/newgame")
	public String newGame(Model model){
		GameEntity ge = new GameEntity();
		model.addAttribute("game", ge);
		return "newgame";
	}
	
	@Transactional
	@RequestMapping(value = "/savegame")
	public String saveGame(Model model,@ModelAttribute("game")GameEntity game) throws ParserConfigurationException, NoVariantsException{

		Variant vs = VariantManager.getVariant("Standard", 1.0f);
		World w = null;
		try {
			w = WorldFactory.getInstance().createWorld(vs);
		} catch (InvalidWorldException e) {
			e.printStackTrace();
		}
		VariantInfo vi1 =new VariantInfo();
		vi1.setVariantName("Standard");
		vi1.setVariantVersion(1.0f);
		w.setVariantInfo(vi1);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user1 = (UserDetails)auth.getPrincipal();
		UserDetailsImpl user = (UserDetailsImpl) user1;
		
		UserEntity ue = us.getUserEntity(user.getId());
		
		game.setW(w);
		HashSet<UserGameEntity> players = new HashSet<UserGameEntity>();
		UserGameEntity uge = new UserGameEntity();
		uge.setGame(game);
		uge.setUser(ue);
		uge.setPower(w.getMap().getPower("England").getName());
		players.add(uge);
		game.setPlayers(players);
	
		ue.addGame(uge);
	
		Session session = sessionFactory.getCurrentSession();
		
		session.save(game);
		session.save(ue);
		session.save(uge);
		
		model.addAttribute("id", game.getId());
		
		return "savegame";

	}
	
}
