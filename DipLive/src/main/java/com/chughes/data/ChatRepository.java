package com.chughes.data;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.Message;

@Repository
public class ChatRepository {
	protected @Autowired SessionFactory sessionFactory;
	
	@Transactional
	public void saveMessage(Message m){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().save(m);
	}
}
