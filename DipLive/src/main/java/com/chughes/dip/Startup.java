package com.chughes.dip;

import java.io.File;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.security.UserDAO;
import com.chughes.security.UserEntity;

import dip.world.variant.VariantManager;

@Component
public class Startup{
	
	@Autowired
	ServletContext context;
	
	protected @Autowired DataSource dataSource;
	protected @Autowired UserDAO us;

	@PostConstruct
	@Transactional
	public void afterPropertiesSet() throws Exception {
		//File vresource = new File("variants");
		//logger.info(vresource.getFile().getAbsolutePath());
		//File one = new File(".");
		//System.out.println(one.getAbsolutePath());
		
		File two = new File( context.getRealPath("/variants") );
		//System.out.println(two.getAbsolutePath());
		VariantManager.init(new File[]{two}, false);
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.execute("create table if not exists UserConnection (userId varchar(255) not null,	providerId varchar(255) not null,	providerUserId varchar(255),	rank int not null,	displayName varchar(255),	profileUrl varchar(512),	imageUrl varchar(512),	accessToken varchar(255) not null,					secret varchar(255),	refreshToken varchar(255),	expireTime bigint,	primary key (userId(100), providerId(50), providerUserId(150)))");
		try{
			template.execute("create unique index UserConnectionRank on UserConnection(userId, providerId, rank)");
		}catch(BadSqlGrammarException e){
			//e.printStackTrace();
		}
		
		UserEntity.NULL_USER = us.getUserEntity(126);
		
		if (UserEntity.NULL_USER == null){
			UserEntity.NULL_USER = new UserEntity();
			UserEntity.NULL_USER.setId(126);
			UserEntity.NULL_USER.setUsername("EMPTY");
		}
		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

//Trying to clean up connections so tomcat doesn't complain. Might have helped with some of the errors, not sure
	
//	@PreDestroy
//	public void cleanUp() {
//		for (Object o : C3P0Registry.getPooledDataSources()) {
//			try {
//				((PooledDataSource) o).close();
//			} catch (Exception e) {
//				// oh well
//			}
//		}
//	}

}
