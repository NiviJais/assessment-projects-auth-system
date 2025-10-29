package com.cloudamize.auth_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudamize.auth_system.entity.User;
import com.cloudamize.auth_system.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class HomeController {
	
	private final CustomUserDetailsService userService;

	@GetMapping("/route1")
	public String accessRoute1() {
		return "Authenticated user access route1!!";
	}
	
	@GetMapping
	public List<User> allUsers() {
				
		return userService.fetchAllUsers();
	}
}
