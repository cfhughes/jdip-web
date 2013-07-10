package com.chughes.dip;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameListController {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	@RequestMapping(value="/gamelist")
	public String listGames(Model model){

		Session hsession = sessionFactory.getCurrentSession();
		Query query = hsession.createQuery("from GameEntity");
		List<GameEntity> games = query.list();
		
		model.addAttribute("games", games);
		
		return "gamelist";
	}
	
	@RequestMapping(value="/join/{gameID}")
	public String join(Model model,@PathVariable(value="gameID") int id){
		
		return "redirect:game/"+id;
	}
}
