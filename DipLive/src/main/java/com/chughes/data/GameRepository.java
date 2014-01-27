package com.chughes.data;

import java.util.List;

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

import dip.world.TurnState;
import dip.world.World;

@Repository
@Scope
public class GameRepository {

	protected @Autowired SessionFactory sessionFactory;
	
	@Transactional(readOnly=true)
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

	@Transactional
	public void updateWorld(World w){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
        sessionFactory.getCurrentSession().update(w);
	}
	
	@Transactional
	public void addTurnstate(int id, TurnState ts){
		World w = (World) sessionFactory.getCurrentSession().get(World.class, id);
		w.setTurnState(ts);
		sessionFactory.getCurrentSession().save(ts);
		//sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
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
	public void deleteInGameUser(UserGameEntity uge){
		sessionFactory.getCurrentSession().delete(uge);
	}
	
	@Transactional
	public void saveGame(GameEntity ge){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().save(ge.getW().getInitialTurnState());
		sessionFactory.getCurrentSession().saveOrUpdate(ge);
		//sessionFactory.getCurrentSession().flush();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<GameEntity> queryGames(int p,int max){
		Query query = sessionFactory.getCurrentSession().createQuery("from GameEntity").setFirstResult(p).setMaxResults(max);
		return query.list();
	}

	@Transactional
	public void updateTS(TurnState ts) {
		sessionFactory.getCurrentSession().update(ts);
	}
	
}
