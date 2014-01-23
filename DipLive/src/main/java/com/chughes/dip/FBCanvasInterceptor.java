package com.chughes.dip;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.web.SignedRequestDecoder;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import com.chughes.security.SimpleSignInAdapter;
import com.chughes.security.UserDAO;

@PropertySource("application.properties")
public class FBCanvasInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Environment environment;

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired 
	private UsersConnectionRepository usersConnectionRepository;

	@Inject
	private UserDAO user;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String clientSecret = environment.getProperty("facebook.clientSecret");
		String clientId = environment.getProperty("facebook.clientId");

		String signedRequest = request.getParameter("signed_request");
		String accessToken = null;
		if (signedRequest != null){
			request.setAttribute("facebook", true);
			SignedRequestDecoder signedRequestDecoder = new SignedRequestDecoder(clientSecret);
			Map<String, ?> decodedSignedRequest = signedRequestDecoder.decodeSignedRequest(signedRequest);
			accessToken = (String) decodedSignedRequest.get("oauth_token");
			if (accessToken == null) {
				//Redirect to authorize page
				response.sendRedirect("fbauth");
				return false;
			}


			OAuth2ConnectionFactory<Facebook> connectionFactory = (OAuth2ConnectionFactory<Facebook>) connectionFactoryLocator.getConnectionFactory(Facebook.class);
			AccessGrant accessGrant = new AccessGrant(accessToken);
			// TODO: Maybe should create via ConnectionData instead?
			Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
			List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);
			
			if (userIds.size() <= 1) {
				usersConnectionRepository.createConnectionRepository(userIds.get(0)).updateConnection(connection);
				SimpleSignInAdapter signInAdapter = new SimpleSignInAdapter(user);
				signInAdapter.signIn(userIds.get(0), connection, null);
			} else {
				// TODO: This should never happen, but need to figure out what to do if it does happen. 
				throw new Exception("Error. More than one user id tied to this account.");
			}
			//debug("Signed in. Redirecting to post-signin page.");
		}
		return true;

	}

}
