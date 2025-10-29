package com.cloudamize.auth_system.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloudamize.auth_system.entity.User;
import com.cloudamize.auth_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
	
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found " + username + " !!"));
		
	}

	@Override
	public void save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setProvider("Local");
		userRepository.save(user);
	}
	
	public User saveGoogleUser(String email, String providerId, String picture) {
		return userRepository.findByUsername(email).orElseGet(()->{
			User newUser = new User();
			newUser.setUsername(email);
			newUser.setPassword(null);
			newUser.setProvider("Google");
			newUser.setProviderId(providerId);
			newUser.setPicture(picture);
			return userRepository.save(newUser);
		});
	}

	@Override
	public List<User> fetchAllUsers() {
		return userRepository.findAll();
	}

}
