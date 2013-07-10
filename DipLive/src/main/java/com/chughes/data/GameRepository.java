package com.chughes.data;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.GameEntity;
import com.chughes.dip.UserGameEntity;

@Repository
public class GameRepository {
	protected @Autowired SessionFactory sessionFactory;
	
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
	
	@Transactional
	public void updateGame(GameEntity ge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
        sessionFactory.getCurrentSession().update(ge);
        sessionFactory.getCurrentSession().flush();
	}
	
	@Transactional
	public void saveInGameUser(UserGameEntity uge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().save(uge);
		sessionFactory.getCurrentSession().flush();
	}
	
	@Transactional
	public void saveGame(GameEntity ge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().save(ge);
		sessionFactory.getCurrentSession().flush();
	}
	
	
}
