package com.chughes.dip.chat;

public class UIChatRequest {
	private int fromid;
	private int lastseen;
	private int gameid;
	
	public int getFromid() {
		return fromid;
	}
	public void setFromid(int fromid) {
		this.fromid = fromid;
	}
	public int getLastseen() {
		return lastseen;
	}
	public void setLastseen(int lastseen) {
		this.lastseen = lastseen;
	}
	public int getGameid() {
		return gameid;
	}
	public void setGameid(int gameid) {
		this.gameid = gameid;
	}
}
