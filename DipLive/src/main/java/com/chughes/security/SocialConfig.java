package com.chughes.security;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleConnectionFactory;

@Configuration
public class SocialConfig {
	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
	    ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
	    registry.addConnectionFactory(new FacebookConnectionFactory("8583553497","f415251f2db9dcb661c5f8a79bd9681b"));
        GoogleConnectionFactory google = new GoogleConnectionFactory(
                "454990989619.apps.googleusercontent.com",
                "jOhrbBz-kjvPl46AA134IuqO");
        google.setScope("https://www.googleapis.com/auth/plus.login");
	    registry.addConnectionFactory(google);
	    return registry;
	}
	
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
	    JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, 
	        connectionFactoryLocator(), Encryptors.noOpText());
	    repository.setConnectionSignUp(new SimpleConnectionSignUp(user));
	    return repository;
	}
	
	@Inject
	private UserDAO user;
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return usersConnectionRepository().createConnectionRepository(((UserDetailsImpl) auth.getPrincipal()).getId()+"");
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Facebook facebook() {
	    return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Google google() {
        Connection<Google> connection = connectionRepository().findPrimaryConnection(Google.class);
        Google google = connection != null ? connection.getApi() : new GoogleTemplate();
        return google;
	}
	
	@Bean
	public ProviderSignInController providerSignInController() {
	    return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(),
	        new SimpleSignInAdapter(user));
	}
	
	@Inject
	private DataSource dataSource;
}
