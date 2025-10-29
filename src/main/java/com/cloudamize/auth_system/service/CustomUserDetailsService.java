package com.cloudamize.auth_system.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cloudamize.auth_system.entity.User;

public interface CustomUserDetailsService extends UserDetailsService{

	public void save(User user);
	
	public User saveGoogleUser(String email, String providerId, String picture);

	public List<User> fetchAllUsers();
}
