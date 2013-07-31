package com.chughes.security;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAO{

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void saveUser(UserDetailsImpl user){
		
		Session session = sessionFactory.getCurrentSession();
		UserEntity ue = new UserEntity();
		ue.setUsername(user.getUsername());
		ue.setPassword(user.getPassword());
		session.save(ue);

	}
	
	@Transactional
	public void updateUser(UserEntity user){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		sessionFactory.getCurrentSession().update(user);
        //sessionFactory.getCurrentSession().flush();
	}

	@Transactional(readOnly=true)
	public UserDetailsImpl getUserByName(String name){
		//System.out.println("NAme is "+name);
		Session session = null;
		UserEntity user = null;
		UserDetailsImpl ud = null;
		//System.out.println("Even Here");

		//System.out.println(sessionFactory);
		session = sessionFactory.getCurrentSession();
		//System.out.println("About Here");
		Query query = session.createQuery("from UserEntity i where i.username = :name");
		query.setString("name", name);
		user = (UserEntity) query.uniqueResult();
		if (user != null){
			ud = new UserDetailsImpl(user.getUsername(),user.getPassword());
			ud.setId(user.getId());
		}


		Logger.getLogger(UserDAO.class).info("Here2");
		return ud;
	}

	@Transactional(readOnly=true)
	public UserEntity getUserEntity(int id){
		Session session = sessionFactory.getCurrentSession();
		UserEntity ue = (UserEntity)session.get(UserEntity.class, id);
		return ue;
	}
	


}
