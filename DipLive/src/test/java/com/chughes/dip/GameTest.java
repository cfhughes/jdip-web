package com.chughes.dip;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/security-app-context.xml")
@WebAppConfiguration
public class GameTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy securityFilter;
	
	@Autowired
	private WebApplicationContext wac;
	
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(this.securityFilter).build();
   
    }
	
	@Test
	public void requestOK() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("I'm not here", "p"));
		mockMvc.perform(get("/")).andExpect(status().isOk());
		
	}
	
	@Test
	public void gameView() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("I'm not here", "p"));
		mockMvc.perform(get("/game/1")).andExpect(status().isOk());
		
	}
	
	@Test
	@Transactional
	public void loginGameCreate() throws Exception{
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("I'm not here", "p"));
		HttpSession session = mockMvc.perform(post("/j_spring_security_check").param("j_username", "p1").param("j_password", "p1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
		
		Assert.assertNotNull(session);
		
		MvcResult result = mockMvc.perform(post("/newgame").session((MockHttpSession)session).locale(Locale.ENGLISH)
				.requestAttr("name", "RobotGameTest").requestAttr("variant", "Standard").requestAttr("turnlength", "0").requestAttr("secret", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
		
		int game_id = (int) result.getModelAndView().getModel().get("gid");
		
		session = mockMvc.perform(post("/j_spring_security_check").param("j_username", "p2").param("j_password", "p2"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
		
		mockMvc.perform(get("/joingame/"+game_id))
                .andExpect(status().isOk());
		
		
	}
	
	
}
