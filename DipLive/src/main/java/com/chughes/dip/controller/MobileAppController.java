package com.chughes.dip.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.UserGameEntity;
import com.chughes.dip.user.UserEntity;

@Controller
public class MobileAppController {
	
	@Autowired private ConnectionFactoryLocator cfl;
	@Autowired private UsersConnectionRepository ucr;
	@Autowired private UserRepository us;
	
	@RequestMapping(value="/JSONauth")
	public @ResponseBody String auth(){
		
		
		return null;
		
	}
	
	@RequestMapping(value = "/JSONmygames")
	public @ResponseBody Map<Integer,String> myGames(Model model,@RequestParam(value="auth") String auth,@RequestParam(value="p") String p){
		OAuth2ConnectionFactory<?> cf = (OAuth2ConnectionFactory<?>) cfl.getConnectionFactory(p);
		
		Connection<?> connect = cf.createConnection(new AccessGrant(auth));
		
		List<String> users = ucr.findUserIdsWithConnection(connect);
		
		System.out.println(users.size());
		
		if (users.get(0) != null){

			UserEntity ue = us.getUserEntity(Integer.parseInt(users.get(0)));
			
			HashMap<Integer, String> result = new HashMap<Integer,String>();
			for (UserGameEntity uge : ue.getGames()) {
				result.put(uge.getGame().getId(), uge.getGame().getName());
			} 
			return result;
		}
		return null;
	}
}
