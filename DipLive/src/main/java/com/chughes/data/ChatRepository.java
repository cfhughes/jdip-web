package com.chughes.data;

import java.util.List;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.Message;
import com.chughes.dip.UIChatRequest;

@Repository
public class ChatRepository {
	protected @Autowired SessionFactory sessionFactory;
	
	@Transactional
	public void saveMessage(Message m){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().saveOrUpdate(m);
	}
	
	@Transactional
	public List<Object> getMessages(int user, UIChatRequest req){
		if (req.getFromid() == -1){
			Query q = sessionFactory.getCurrentSession().createQuery("select m.id,m.text,m.from.id,m.to.id from UserGameEntity u join u.messages m where u.game.id = :g and m.id > :l and m.to.id is null");
			q.setInteger("g", req.getGameid());
			q.setInteger("l", req.getLastseen());
			return q.list();
		}
		Query q = sessionFactory.getCurrentSession().createQuery("select m.id,m.text,m.from.id,m.to.id from UserGameEntity u join u.messages m where u.user.id = :u and u.game.id = :g and (m.from.id = :f or m.to.id = :f) and m.id > :l");
		q.setInteger("u", user);
		q.setInteger("g", req.getGameid());
		q.setInteger("f", req.getFromid());
		q.setInteger("l", req.getLastseen());
		return q.list();
	}
}
