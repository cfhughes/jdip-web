package com.chughes.dip;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

	@RequestMapping(value="/game/JSONchat")
	public @ResponseBody Map<String, ?> chat(@RequestBody UIChat chat){
		return null;
	}
}
