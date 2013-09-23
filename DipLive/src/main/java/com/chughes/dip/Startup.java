package com.chughes.dip;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;

import dip.world.variant.VariantManager;

@Component
public class Startup{

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		File vresource = new File("/home/chris/variants");
		//logger.info(vresource.getFile().getAbsolutePath());

		VariantManager.init(new File[]{vresource}, false);

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
