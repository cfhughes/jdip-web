package com.chughes.dip;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;
import com.chughes.security.UserEntity;

import dip.world.InvalidWorldException;
import dip.world.World;
import dip.world.WorldFactory;
import dip.world.World.VariantInfo;
import dip.world.variant.NoVariantsException;
import dip.world.variant.VariantManager;
import dip.world.variant.data.Variant;

@Controller
public class NewGameController {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    UserDAO us;
	
	@Transactional
	@RequestMapping(value = "/newgame")
	public String newGame(Model model) throws ParserConfigurationException, NoVariantsException{
		File vresource = new File("C:/variants");
		//logger.info(vresource.getFile().getAbsolutePath());

		VariantManager.init(new File[]{vresource}, false);

		Variant vs = VariantManager.getVariant("Standard", 1.0f);
		World w = null;
		try {
			w = WorldFactory.getInstance().createWorld(vs);
		} catch (InvalidWorldException e) {
			// TODO Auto-generated catch block
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
		
		GameEntity g = new GameEntity();
		g.setName("A New Game");
		g.setW(w);
		HashSet<UserGameEntity> players = new HashSet<UserGameEntity>();
		UserGameEntity uge = new UserGameEntity();
		uge.setGame(g);
		uge.setUser(ue);
		uge.setPower(w.getMap().getPower("England").getName());
		players.add(uge);
		g.setPlayers(players);
	
		ue.addGame(uge);
	
		Session session = sessionFactory.getCurrentSession();
		
		session.save(g);
		session.save(ue);
		session.save(uge);
		
		model.addAttribute("id", g.getId());
		
		return "newgame";
	}
	
}
