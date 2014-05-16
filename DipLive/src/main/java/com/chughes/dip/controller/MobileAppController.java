package com.chughes.dip.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chughes.dip.data.GameRepository;
import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.GameInfo;
import com.chughes.dip.game.GameInfo.GameInfoPlayer;
import com.chughes.dip.game.UserGameEntity;
import com.chughes.dip.user.UserDetailsImpl;
import com.chughes.dip.user.UserEntity;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;

import dip.world.Power;

@Controller
public class MobileAppController {

	@Autowired private ConnectionFactoryLocator cfl;
	@Autowired private UsersConnectionRepository ucr;
	@Autowired private UserRepository us;
	@Autowired private RememberMeServices rms;
	@Autowired private GameRepository gameRepo;

	@RequestMapping(value="/JSONauthtest")
	public @ResponseBody String test(){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			return "loggedin";
		}
		return "anon";
	}

	@RequestMapping(value="/JSONoauth")
	public @ResponseBody String OAuth(@RequestParam(value="auth") String auth,@RequestParam(value="p") String p,HttpServletRequest request, HttpServletResponse response){
		OAuth2ConnectionFactory<?> cf = (OAuth2ConnectionFactory<?>) cfl.getConnectionFactory(p);

		Connection<?> connect = cf.createConnection(new AccessGrant(auth));

		List<String> users = ucr.findUserIdsWithConnection(connect);

		if (users.size() == 1){

			UserDetailsImpl user = new UserDetailsImpl(users.get(0));
			UserEntity u = us.getUserEntity(user.getId());
			user.setUsername(u.getUsername());
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(token);

			HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request ) {
				@Override public String getParameter(String name) { return "true"; }            
			};

			rms.loginSuccess(wrapper, response, token); 
			return "success";
		}
		return "fail";

	}

	@RequestMapping(value = "/JSONmygames")
	@PreAuthorize("hasRole('PLAYER')")
	public @ResponseBody Map<Integer,Map<String,String>> myGames(Model model){

		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;

			UserEntity ue = us.getUserEntity(user.getId());

			Map<Integer, Map<String, String>> result = new HashMap<Integer,Map<String,String>>();
			for (UserGameEntity uge : ue.getGames()) {
				Map<String,String> info = new HashMap<String,String>();
				info.put("name", uge.getGame().getName());
				info.put("ready", uge.isReady()+"");
				info.put("newmessage", uge.isUnread()+"");
				info.put("phase", uge.getGame().getPhase());
				result.put(uge.getGame().getId(), info);
			} 
			return result;
		}
		return null;
	}

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value = "/game/{gameId}/JSONgameinfo")
	public @ResponseBody GameInfo gameInfo(@PathVariable(value="gameId") int id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl user = (UserDetailsImpl)auth.getPrincipal();
		UserGameEntity uge = gameRepo.inGameUser(id, user.getId());

		Set<UserGameEntity> players = uge.getGame().getPlayers();
		GameInfoPlayer[] p = new GameInfoPlayer[players.size()];

		GameInfo info = new GameInfo();
		int i = 0;
		for (UserGameEntity player : players) {
			p[i] = info.new GameInfoPlayer();
			p[i].setId(player.getId());
			p[i].setPower(player.getPower());
			i++;
		}
		info.setPlayers(p);
		return info;
	}
}
