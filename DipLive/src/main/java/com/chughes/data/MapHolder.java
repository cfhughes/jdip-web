package com.chughes.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import dip.gui.map.DefaultMapRenderer2;
import dip.world.Phase;

@Repository
@Scope(value="session")
public class MapHolder implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1838207806369428656L;
	private Map<Integer,GameCache> games = new HashMap<Integer,GameCache>();
	
	public void setMr(int id, DefaultMapRenderer2 mr) {
		if (!games.containsKey(id)){
			games.put(id, new GameCache());
		}
		games.get(id).setMr(mr);
		
	}

	public void setPhase(int id, Phase phase) {
		if (!games.containsKey(id)){
			games.put(id, new GameCache());
		}
		games.get(id).setPhase(phase);
		
	}

	public Phase getPhase(int id) {
		return games.get(id).getPhase();
	}

	public DefaultMapRenderer2 getMr(int id) {
		return games.get(id).getMr();
	}

	
	private class GameCache implements Serializable {

		private static final long serialVersionUID = 8048372343061232482L;
		
		public DefaultMapRenderer2 getMr() {
			return mr;
		}
		public void setMr(DefaultMapRenderer2 mr) {
			this.mr = mr;
		}
		public Phase getPhase() {
			return phase;
		}
		public void setPhase(Phase phase) {
			this.phase = phase;
		}
//		public GameEntity getGame() {
//			return game;
//		}
//		public void setGame(GameEntity game) {
//			this.game = game;
//		}
		private DefaultMapRenderer2 mr; 
		private Phase phase;
		//private GameEntity game;
	}
}
