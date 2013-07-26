package com.chughes.dip;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.chughes.security.UserEntity;

@Entity
public class Message {
	private String text;
	private int id;
	private UserEntity from;
	
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
	public UserEntity getFrom() {
		return from;
	}
	public void setFrom(UserEntity from) {
		this.from = from;
	}
	

}
