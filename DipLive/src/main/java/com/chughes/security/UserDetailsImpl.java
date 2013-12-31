package com.chughes.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1556832297202500775L;
	/**
	 * 
	 */
	@Size(min=2,message="Username must be at least 2 characters")
	private String username;
	@Size(min=5,message="Password must be at least 5 characters")
    private String password;
    @Email(message="Please provide a valid email address")
    @NotBlank
    private String email;
    private int id;

	public UserDetailsImpl(String uname, String pword) {
		username = uname;
		password = pword;
	}

	public UserDetailsImpl() {
	}

	public UserDetailsImpl(String userId) {
		id = Integer.parseInt(userId);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> l1 = new ArrayList<GrantedAuthority>();

        l1.add(new SimpleGrantedAuthority("PLAYER"));

        return l1;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
