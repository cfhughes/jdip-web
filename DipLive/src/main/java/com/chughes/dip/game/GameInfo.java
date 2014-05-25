package com.chughes.dip.game;

public class GameInfo {
	private GameInfoPlayer[] players;
	//this player's id
	private int me;

	public GameInfoPlayer[] getPlayers() {
		return players;
	}

	public void setPlayers(GameInfoPlayer[] players) {
		this.players = players;
	}

	public int getMe() {
		return me;
	}

	public void setMe(int me) {
		this.me = me;
	}

	public class GameInfoPlayer {

		private int id;
		private String power;
		private String username;

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getPower() {
			return power;
		}
		public void setPower(String power) {
			this.power = power;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}

	}

}
