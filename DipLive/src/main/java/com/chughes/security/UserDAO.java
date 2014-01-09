package com.chughes.security;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAO{

	@Autowired private SessionFactory sessionFactory;
	@Autowired private BCryptPasswordEncoder encoder;

	@Transactional
	public void saveUser(UserDetailsImpl user) throws Exception{
		
		Session session = sessionFactory.getCurrentSession();
		UserEntity ue = new UserEntity();
		if (user.getUsername().length() < 2){
			throw new Exception("Username must be at least 2 characters");
		}else if (user.getPassword().length() < 5){
			throw new Exception("Password must be at least 5 characters");
		}else if (getUserByName(user.getUsername()) != null){
			throw new Exception("Username Taken");
		}
		ue.setUsername(user.getUsername());
		ue.setPassword(encoder.encode(user.getPassword()));
		session.save(ue);

	}
	
	@Transactional
	public Serializable saveUser(UserEntity ue){
		Session session = sessionFactory.getCurrentSession();
		return session.save(ue);
	}
	
	@Transactional
	public void updateUser(UserEntity user){
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.AUTO);
		//merge is only necessary for detached NULL_USER, are there disadvantages to using merge()?
		sessionFactory.getCurrentSession().merge(user);
        //sessionFactory.getCurrentSession().flush();
	}
	
	@Transactional
	public void editUser(UserEntity user){

		sessionFactory.getCurrentSession().update(user);

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
		Query query = session.createQuery("from UserEntity i where i.username = :name and i.password is not null");
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
