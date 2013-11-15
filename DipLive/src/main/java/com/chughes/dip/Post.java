package com.chughes.dip;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.chughes.security.UserEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Post implements Comparable<Post> {
	
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
	@Column(length=1000)
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@ManyToOne
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
	@OneToMany
	@Sort(type=SortType.NATURAL)
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
	@Override
	public int compareTo(Post o) {
		return this.timestamp.compareTo(o.timestamp);
	}

}
