package com.chughes.dip.game;

public class GameInfo {
	private GameInfoPlayer[] players;

	public GameInfoPlayer[] getPlayers() {
		return players;
	}

	public void setPlayers(GameInfoPlayer[] players) {
		this.players = players;
	}

	public class GameInfoPlayer {

		private int id;
		private String power;

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

	}

}
