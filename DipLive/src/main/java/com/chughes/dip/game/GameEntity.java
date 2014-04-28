package com.chughes.dip.game;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;

import dip.world.World;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GameEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6485361960198906888L;
	public enum Stage {PREGAME,PLAYING,ENDED};
	
	private Stage stage;
	private Set<UserGameEntity> players = new HashSet<UserGameEntity>();
	private World w;
	private boolean crashed = false;
	private int id;
	@NotEmpty
	private String name;
	private int maxplayers;
	private String secret;
	private String phase;
	@Min(0)
	@Max(336)
	private int turnlength = 0;//hours
	private Date turnend;
	private int level;
	
	public int getTurnlength() {
		return turnlength;
	}
	public void setTurnlength(int turnlength) {
		this.turnlength = turnlength;
	}
	public Date getTurnend() {
		return turnend;
	}
	public void setTurnend(Date turnend) {
		this.turnend = turnend;
	}
	//Fetching Eagerly to help Async Methods, but there is probably a better way
	@OneToMany(fetch = FetchType.EAGER)
	public Set<UserGameEntity> getPlayers() {
		return players;
	}
	public void setPlayers(Set<UserGameEntity> players) {
		this.players = players;
	}
	@OneToOne
	@Cascade({CascadeType.SAVE_UPDATE})
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
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
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
	public boolean isCrashed() {
		return crashed;
	}
	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
