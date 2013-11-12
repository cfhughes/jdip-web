package com.chughes.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SessionLogger implements AuthenticationSuccessHandler {
	
	@Autowired UserDAO us;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {
		String ip = request.getRemoteAddr();
		UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
		UserEntity ue = us.getUserEntity(user.getId());
		ue.getIps().add(ip);
		us.saveUser(ue);
	}

}
