package com.chughes.dip;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.chughes.security.UserEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Message {
	private String text;
	private int id;
	private UserGameEntity from;
	private UserGameEntity to;
	private Date timestamp;
	
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
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	

}
