package com.chughes.dip;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import dip.world.World;

@Entity
public class GameEntity {
	public enum Stage {PREGAME,PLAYING,ENDED};
	
	private Stage stage;
	private Set<UserGameEntity> players = new HashSet<UserGameEntity>();
	private World w;
	private int id;
	private String name;
	private int maxplayers;
	private String secret;
	
	//Fetching Eagerly to help Async Methods, but there is probably a better way
	@OneToMany(fetch = FetchType.EAGER)
	public Set<UserGameEntity> getPlayers() {
		return players;
	}
	public void setPlayers(Set<UserGameEntity> players) {
		this.players = players;
	}
	@Lob
	public World getW() {
		return w;
	}
	public void setW(World w) {
		this.w = w;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxplayers() {
		return maxplayers;
	}
	public void setMaxplayers(int maxplayers) {
		this.maxplayers = maxplayers;
	}
	@Enumerated(EnumType.STRING)
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	//for private games
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameEntity other = (GameEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
