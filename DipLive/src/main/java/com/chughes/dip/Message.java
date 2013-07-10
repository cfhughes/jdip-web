package com.chughes.dip;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Message {
	private String text;
	private int id;
	private UserGameEntity from;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@OneToOne
	public UserGameEntity getFrom() {
		return from;
	}
	public void setFrom(UserGameEntity from) {
		this.from = from;
	}
	

}
