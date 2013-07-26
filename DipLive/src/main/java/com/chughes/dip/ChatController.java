package com.chughes.dip;

import java.util.Map;

import javax.servlet.http.HttpSession;

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
	public @ResponseBody Map<String, ?> chat(@RequestBody UIChat chat, HttpSession session){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;

			//UserEntity ue = us.getUserEntity(user.getId());


			System.out.println(user.getUsername()+" is Logged In");


			Message m = new Message();
			m.setText(chat.getMessage());
			m.setFrom(us.getUserEntity(user.getId()));
			
			cr.saveMessage(m);
			UserGameEntity gu = gr.inGameUser(Integer.parseInt(chat.getTo()));
			gu.getMessages().add(m);
			gr.saveInGameUser(gu);
			
		}
		return null;
	}
}
