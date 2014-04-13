package com.chughes.dip.misc;

import java.io.PrintStream;
import java.io.StringWriter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandling {
	
	@Autowired
	private MailSender mailSender;
	@Autowired
    private SimpleMailMessage message;
	
	@ExceptionHandler
	public ModelAndView handle(Exception e){
		
		SimpleMailMessage msg = new SimpleMailMessage(this.message);
		msg.setTo("falling2rock@gmail.com");
		msg.setText(ExceptionUtils.getStackTrace(e));
		mailSender.send(msg);
		
		
		ModelAndView m = new ModelAndView("exception");
		m.addObject("description", e.getLocalizedMessage());
		return m;
	}


}
