package com.chughes.dip.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.chughes.dip.chat.Message;
import com.chughes.dip.user.UserEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGameEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8784807600063349365L;
	private List<Message> messages;
	private Map<Integer,Long> readlog = new HashMap<Integer,Long>();
	private UserEntity user;
	private GameEntity game;
	private String power;
	private int id;
	private boolean ready;
	private boolean orderable;
	private boolean unread;
	private float victory_share = -1;
	private int supply_centers;
	private int missed;
	private String color;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	@ManyToOne(optional = false)
	public GameEntity getGame() {
		return game;
	}
	public void setGame(GameEntity game) {
		this.game = game;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	@ManyToMany
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}	
	public float getVictory_share() {
		return victory_share;
	}
	public void setVictory_share(float victory_share) {
		this.victory_share = victory_share;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result + id;
		result = prime * result + ((power == null) ? 0 : power.hashCode());
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
		UserGameEntity other = (UserGameEntity) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (id != other.id)
			return false;
		if (power == null) {
			if (other.power != null)
				return false;
		} else if (!power.equals(other.power))
			return false;
		return true;
	}
	public int getSupply_centers() {
		return supply_centers;
	}
	public void setSupply_centers(int supply_centers) {
		this.supply_centers = supply_centers;
	}
	public int getMissed() {
		return missed;
	}
	public void setMissed(int missed) {
		this.missed = missed;
	}
	@ElementCollection
	public Map<Integer, Long> getReadlog() {
		return readlog;
	}
	public void setReadlog(Map<Integer, Long> readlog) {
		this.readlog = readlog;
	}
	public boolean isOrderable() {
		return orderable;
	}
	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}
	@Transient
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	
}
