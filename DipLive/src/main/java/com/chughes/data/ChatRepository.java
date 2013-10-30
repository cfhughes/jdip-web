package com.chughes.data;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.Message;
import com.chughes.dip.Post;
import com.chughes.dip.UIChatRequest;

@Repository
public class ChatRepository {
	protected @Autowired SessionFactory sessionFactory;
	
	@Transactional
	public Post getTopic(int id){
		return (Post) sessionFactory.getCurrentSession().get(Post.class, id);
	}
	
	@Transactional
	public List<Post> getTopics(){
		Query q = sessionFactory.getCurrentSession().createQuery("from Post where toplevel = true order by timestamp desc");
		q.setMaxResults(15);
		return q.list();
	}
	
	public void savePost(Post p){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().save(p);
	}
	
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
