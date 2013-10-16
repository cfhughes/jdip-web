package com.chughes.dip;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;

import dip.world.variant.VariantManager;

@Component
public class Startup{
	
	@Autowired
	ServletContext context;

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		//File vresource = new File("variants");
		//logger.info(vresource.getFile().getAbsolutePath());
		//File one = new File(".");
		//System.out.println(one.getAbsolutePath());
		
		File two = new File( context.getRealPath("/variants") );
		//System.out.println(two.getAbsolutePath());
		VariantManager.init(new File[]{two}, false);

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
