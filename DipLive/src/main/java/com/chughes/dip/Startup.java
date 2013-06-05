package com.chughes.dip;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import dip.world.variant.VariantManager;

@Component
public class Startup{

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		File vresource = new File("C:/variants");
		//logger.info(vresource.getFile().getAbsolutePath());

		VariantManager.init(new File[]{vresource}, false);
		
	}

}
