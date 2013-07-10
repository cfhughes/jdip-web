package com.chughes.dip;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import dip.world.World;

@Entity
public class GameEntity {
	
	private Set<UserGameEntity> players = new HashSet<UserGameEntity>();
	private World w;
	private int id;
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
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
	
}
