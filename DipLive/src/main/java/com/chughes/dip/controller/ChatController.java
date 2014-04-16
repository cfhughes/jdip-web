package com.chughes.dip.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chughes.dip.chat.Message;
import com.chughes.dip.chat.Post;
import com.chughes.dip.chat.UIChat;
import com.chughes.dip.chat.UIChatRequest;
import com.chughes.dip.data.ChatRepository;
import com.chughes.dip.data.GameRepository;
import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.UserGameEntity;
import com.chughes.dip.user.UserDetailsImpl;
import com.chughes.dip.user.UserEntity;

@Controller
public class ChatController {

	@Autowired UserRepository us;
	@Autowired ChatRepository cr;
	@Autowired GameRepository gr;

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value="/forum")
	public String forum(Model m){
		m.addAttribute("topics", cr.getTopics());
		return "forum";
	}

	@PreAuthorize("hasRole('PLAYER')")
	@RequestMapping(value="/JSONchat")
	public @ResponseBody String post(@RequestBody UIChat chat){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetailsImpl){
			UserDetailsImpl user = (UserDetailsImpl)auth.getPrincipal();
			UserEntity ue = us.getUserEntity(user.getId());

			Post p = new Post();
			p.setAuthor(ue);
			//strip html and save
			p.setText(chat.getMessage().replaceAll("\\<.*?>",""));
			p.setTimestamp(new Date());
			System.out.println(chat.getTo());
			if (chat.getTo() != -1){
				cr.savePost(p);
				Post parent = cr.getTopic(chat.getTo());
				parent.getReplies().add(p);
				cr.savePost(parent);
			}else{
				p.setToplevel(true);
				p.setSubject(chat.getSubject());
				cr.savePost(p);
			}

			return "success";
		}
		return "fail";
	}

	@RequestMapping(value="/game/JSONchat")
	public @ResponseBody Map<String, Integer> chat(@RequestBody UIChat chat){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;

			//UserEntity ue = us.getUserEntity(user.getId());


			//System.out.println(user.getUsername()+" is Logged In");

			UserGameEntity uge = gr.inGameUser(chat.getGameid(), user.getId());
			if (uge != null){
				UserGameEntity gu = null;
				if (chat.getTo() != -1){
					gu = gr.inGameUser(chat.getTo());
				}
				if (uge.getGame().getPlayers().contains(gu) || chat.getTo() == -1){
					Message m = new Message();
					m.setText(chat.getMessage());
					m.setTimestamp(new Date());
					m.setFrom(uge);
					uge.getMessages().add(m);

					if (gu != null){
						gu.getMessages().add(m);
						gu.setUnread(true);
						m.setTo(gu);
					}

					cr.saveMessage(m);
					gr.saveInGameUser(uge);
					if (gu != null)gr.saveInGameUser(gu);
					return Collections.singletonMap("success", 1);
				}
			}
		}
		return null;
	}

	@RequestMapping(value="/game/JSONmessages")
	public @ResponseBody List<SortedMap<String, String>> retreiveChat(@RequestBody UIChatRequest request){
		UserDetailsImpl user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails){
			UserDetails user1 = (UserDetails)auth.getPrincipal();
			user = (UserDetailsImpl) user1;
			UserGameEntity uge = gr.inGameUser(request.getGameid(), user.getId());
			if (uge != null){
				List<SortedMap<String,String>> result = new ArrayList<SortedMap<String,String>>();
				uge.setUnread(false);
				List<Object[]> re = cr.getMessages(user.getId(), request);
				Map<Integer, Long> read = uge.getReadlog();
				for (Object[] m : re) {
					if (m.length == 5){
						SortedMap<String,String> mess = new TreeMap<String,String>();
						mess.put("id", ((Integer) m[0]).toString());
						mess.put("text", (String) m[1]);
						mess.put("fromid", ((Integer) m[2]).toString());
						mess.put("fromuser", (String) m[3]);
						if (m[4] != null){
							if (read.get(request.getFromid()) == null || ((Date) m[4]).getTime() > read.get(request.getFromid()))mess.put("new", "true");
							else mess.put("new", "false");
							mess.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format((Date) m[4]));
						}else{
							mess.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(959212800l)));
						}
						result.add(mess);
					}
				}
				read.remove(request.getFromid());
				read.put(request.getFromid(), new Date().getTime());
				gr.saveInGameUser(uge);
				return result;
			}
		}
		return null;
	}
}
