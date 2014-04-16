package com.chughes.dip.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import com.chughes.dip.data.UserRepository;

public final class SimpleSignInAdapter implements SignInAdapter {
	
	//private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator();
	private UserRepository userR;

	public SimpleSignInAdapter(UserRepository userR) {
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
