package com.chughes.dip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class Mailer {
	
	@Autowired
	private MailSender mailSender;
	@Autowired
    private SimpleMailMessage message;
	
	public void newphase(String email, String game){
		SimpleMailMessage msg = new SimpleMailMessage(this.message);
		msg.setTo(email);
		msg.setSubject("New Phase in "+game);
		msg.setText("Your game has advanced a phase, login soon to plan your next moves.");
		mailSender.send(msg);
	}

}
