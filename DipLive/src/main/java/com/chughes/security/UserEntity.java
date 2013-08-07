package com.chughes.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.chughes.dip.UserGameEntity;

@Entity
public class UserEntity {

	private int id;
	private String username;
	private String password;
	private Set<UserGameEntity> games = new HashSet<UserGameEntity>();
	
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

}
