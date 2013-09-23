package com.chughes.data;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.GameEntity;
import com.chughes.dip.UserGameEntity;

import dip.gui.map.DefaultMapRenderer2;

@Repository
public class GameRepository {
	protected @Autowired SessionFactory sessionFactory;
	
	private DefaultMapRenderer2 mr; 
	
	@Transactional(readOnly = true)
	public GameEntity findById(int id){
		return (GameEntity) sessionFactory.getCurrentSession().get(GameEntity.class, id);		
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

	public DefaultMapRenderer2 getMr() {
		return mr;
	}

	public void setMr(DefaultMapRenderer2 mr) {
		this.mr = mr;
	}
	
	
}
