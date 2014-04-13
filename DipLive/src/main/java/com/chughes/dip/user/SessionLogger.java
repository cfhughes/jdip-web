package com.chughes.dip.user;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SessionLogger implements AuthenticationSuccessHandler {
	
	@Autowired UserDAO us;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {
		String ip = request.getRemoteAddr();
		System.out.println(ip);
		UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
		UserEntity ue = us.getUserEntity(user.getId());
		ue.getIps().add(ip);
		us.saveUser(ue);
		DefaultRedirectStrategy redirect = new DefaultRedirectStrategy();
		redirect.sendRedirect(request, response, "/");
	}

}
