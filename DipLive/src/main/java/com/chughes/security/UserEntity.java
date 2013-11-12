package com.chughes.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.swing.Spring;

import com.chughes.dip.UserGameEntity;

@Entity
public class UserEntity {

	private int id;
	private String username;
	private String password;
	private String email;
	private Set<UserGameEntity> games = new HashSet<UserGameEntity>();
	private Set<String> ips = new HashSet<String>();
	private int wins;
	private int losses;
	
	
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@OneToMany
	public Set<UserGameEntity> getGames() {
		return games;
	}
	public void setGames(Set<UserGameEntity> games) {
		this.games = games;
	}
	
	public void addGame(UserGameEntity g){
		games.add(g);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@ElementCollection
	public Set<String> getIps() {
		return ips;
	}
	public void setIps(Set<String> ips) {
		this.ips = ips;
	}

}
