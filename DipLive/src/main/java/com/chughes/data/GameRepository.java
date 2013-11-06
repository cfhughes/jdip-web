package com.chughes.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.GameEntity;
import com.chughes.dip.UserGameEntity;

import dip.gui.map.DefaultMapRenderer2;
import dip.world.Phase;
import dip.world.TurnState;
import dip.world.World;

@Repository
@Scope(value="session")
public class GameRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4156517874604991541L;

	protected @Autowired SessionFactory sessionFactory;
	
	private Map<Integer,GameCache> games = new HashMap<Integer,GameCache>();
	
	@Transactional
	public GameEntity findById(int id){
		GameEntity ge = (GameEntity) sessionFactory.getCurrentSession().get(GameEntity.class, id);
		return ge;
	}
	
	@Transactional(readOnly = true)
	public UserGameEntity inGameUser(int game,int user){
		Query query = sessionFactory.getCurrentSession().createQuery("from UserGameEntity where game_id = :g and user_id = :u");
		query.setInteger("g", game);
		query.setInteger("u", user);
		return (UserGameEntity) query.uniqueResult();
	}
	
	@Transactional(readOnly = true)
	public UserGameEntity inGameUser(int id){
		return (UserGameEntity) sessionFactory.getCurrentSession().get(UserGameEntity.class, id);
	}

	@Async
	@Transactional
	public void updateWorld(World w){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
        sessionFactory.getCurrentSession().update(w);
	}
	
	@Transactional
	public void updateGame(GameEntity ge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
        sessionFactory.getCurrentSession().update(ge);
        //sessionFactory.getCurrentSession().flush();
	}
	
	@Transactional
	public void saveInGameUser(UserGameEntity uge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().saveOrUpdate(uge);
		//sessionFactory.getCurrentSession().flush();
	}
	
	@Transactional
	public void saveGame(GameEntity ge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().saveOrUpdate(ge);
		//sessionFactory.getCurrentSession().flush();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<GameEntity> queryGames(){
		Query query = sessionFactory.getCurrentSession().createQuery("from GameEntity");
		return query.list();
	}
	
	private class GameCache implements Serializable {
		
		private static final long serialVersionUID = -7088059463120248898L;

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
	
}
