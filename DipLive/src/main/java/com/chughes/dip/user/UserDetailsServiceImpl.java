package com.chughes.dip.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chughes.dip.data.UserRepository;

@Service(value="customUserService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    UserRepository us;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		UserDetailsImpl user = us.getUserByName(name);
		if (user == null){
			throw new UsernameNotFoundException("Not Found:"+name);
		}else{
			return user;
		}
	}

}
