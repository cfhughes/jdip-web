package com.chughes.dip;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chughes.data.ChatRepository;
import com.chughes.data.GameRepository;
import com.chughes.security.UserDAO;
import com.chughes.security.UserDetailsImpl;

@Controller
public class ChatController {

	@Autowired UserDAO us;
	@Autowired ChatRepository cr;
	@Autowired GameRepository gr;

	@RequestMapping(value="/game/JSONchat")
	public @ResponseBody Map<String, Integer> chat(@RequestBody UIChat chat){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;

			//UserEntity ue = us.getUserEntity(user.getId());


			System.out.println(user.getUsername()+" is Logged In");

			UserGameEntity uge = gr.inGameUser(chat.getGameid(), user.getId());
			if (uge != null){ 
				UserGameEntity gu = gr.inGameUser(chat.getTo());
				if (uge.getGame().getPlayers().contains(gu)){
					Message m = new Message();
					m.setText(chat.getMessage());

					m.setFrom(uge);
					uge.getMessages().add(m);

					gu.getMessages().add(m);
					m.setTo(gu);

					cr.saveMessage(m);
					gr.saveInGameUser(uge);
					gr.saveInGameUser(gu);
					return Collections.singletonMap("success", 1);
				}
			}
		}
		return null;
	}

	@RequestMapping(value="/game/JSONmessages")
	public @ResponseBody List<Object> retreiveChat(@RequestBody UIChatRequest request){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;
			if (gr.inGameUser(request.getGameid(), user.getId()) != null){
				return cr.getMessages(user.getId(), request);
			}
		}
		return null;
	}
}
