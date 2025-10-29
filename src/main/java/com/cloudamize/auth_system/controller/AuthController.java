package com.cloudamize.auth_system.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudamize.auth_system.auth.JwtUtil;
import com.cloudamize.auth_system.dtos.AuthRequest;
import com.cloudamize.auth_system.dtos.AuthResponse;
import com.cloudamize.auth_system.entity.User;
import com.cloudamize.auth_system.exception.CustomException;
import com.cloudamize.auth_system.service.CustomUserDetailsService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
	
	private final CustomUserDetailsService userDetailsService;
	
	private final JwtUtil jwtUtil;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user){
		userDetailsService.save(user);
		return ResponseEntity.ok("User registered successfully");
	}
	
	@PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
		log.info("request : {} " , request);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        log.info("userDetails : {}", userDetails);
        String token = jwtUtil.generateToken(userDetails.getUsername());
        log.info("token : {}", token);
        return ResponseEntity.ok(new AuthResponse(token));
    }

	
	@GetMapping("/logout/google")
	public void googleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException, java.io.IOException {
	    request.getSession().invalidate();
	    SecurityContextHolder.clearContext();

	    // Clear cookies
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            cookie.setValue("");
	            cookie.setPath("/");
	            cookie.setMaxAge(0);
	            response.addCookie(cookie);
	        }
	    }

	    // Redirect to Google's logout URL (forces sign-out)
	    String googleLogoutUrl = "https://accounts.google.com/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/login.html";
	    response.sendRedirect(googleLogoutUrl);
	}


}
