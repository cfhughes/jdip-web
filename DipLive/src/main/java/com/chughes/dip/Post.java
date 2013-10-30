package com.chughes.dip;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.chughes.security.UserEntity;

@Entity
public class Post {
	
	private int id;
	private String text;
	private UserEntity author;
	private String subject;
	private Date timestamp;
	private SortedSet<Post> replies = new TreeSet<Post>();
	private boolean toplevel = false;  
	
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
	public UserEntity getAuthor() {
		return author;
	}
	public void setAuthor(UserEntity author) {
		this.author = author;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public SortedSet<Post> getReplies() {
		return replies;
	}
	public void setReplies(SortedSet<Post> replies) {
		this.replies = replies;
	}
	public boolean isToplevel() {
		return toplevel;
	}
	public void setToplevel(boolean toplevel) {
		this.toplevel = toplevel;
	}

}
