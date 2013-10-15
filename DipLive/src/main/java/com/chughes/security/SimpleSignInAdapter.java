package com.chughes.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public final class SimpleSignInAdapter implements SignInAdapter {
	
	//private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator();
	private UserDAO userR;

	public SimpleSignInAdapter(UserDAO userR) {
		super();
		this.userR = userR;
	}

	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		UserDetailsImpl user = new UserDetailsImpl(userId);
		UserEntity u = userR.getUserEntity(user.getId());
		user.setUsername(u.getUsername());
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
		//userCookieGenerator.addCookie(userId, request.getNativeResponse(HttpServletResponse.class));
		return null;
	}

}
