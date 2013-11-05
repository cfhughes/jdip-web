package com.chughes.dip;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.chughes.security.UserEntity;

@Entity
public class Message {
	private String text;
	private int id;
	private UserGameEntity from;
	private UserGameEntity to;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=500)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@ManyToOne
	public UserGameEntity getFrom() {
		return from;
	}
	public void setFrom(UserGameEntity from) {
		this.from = from;
	}
	@ManyToOne
	public UserGameEntity getTo() {
		return to;
	}
	public void setTo(UserGameEntity to) {
		this.to = to;
	}
	

}
