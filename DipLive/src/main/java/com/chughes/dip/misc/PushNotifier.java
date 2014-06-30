package com.chughes.dip.misc;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@PropertySources(value = {@PropertySource("classpath:application.properties")})
public class PushNotifier {

	ClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
	
	@Autowired
    private Environment environment;

	@Async
	public void push(String reg,String name){
		try {
			ClientHttpRequest req = f.createRequest(new URI("https://android.googleapis.com/gcm/send"), HttpMethod.POST);
			req.getHeaders().add("Authorization", "key="+environment.getProperty("gcm.key"));
			req.getHeaders().add("Content-Type", "application/json");
			OutputStream s = req.getBody();
			IOUtils.write("{\"registration_ids\":[\""+reg+"\"],\"data\":{\"message\":\"Your Game, "+name+", has advanced to a new phase\"}}", s);
			req.execute();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
	}

}
