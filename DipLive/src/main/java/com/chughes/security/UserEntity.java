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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.chughes.dip.UserGameEntity;

@Entity
public class UserEntity {
	
	public static UserEntity NULL_USER;

	private int id;
	@Size(min=2,message="Username must be at least 2 characters")
	private String username;
	private String password;
    @Email(message="Please provide a valid email address")
	private String email;
	private Set<UserGameEntity> games = new HashSet<UserGameEntity>();
	private Set<String> ips = new HashSet<String>();
	private int wins;
	private int losses;
	private int retreats;
	
	
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
	public int getRetreats() {
		return retreats;
	}
	public void setRetreats(int retreats) {
		this.retreats = retreats;
	}

}
