package com.cloudamize.auth_system.auth;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cloudamize.auth_system.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
	
	private final JwtUtil jwtUtil; 
	
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String authHeader = request.getHeader("Autharization");
		
		 String username = null;
	     String token = null;
	     
	     if(authHeader != null && authHeader.startsWith("Bearer")) {
	    	 token = authHeader.substring(7);
	    	 username = jwtUtil.extractUsername(token);
	     }
	     
	     if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	    	 UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
	    	 if(jwtUtil.isTokenValid(token, userDetails.getUsername()) ) {
	    		 UsernamePasswordAuthenticationToken authToken = 
	    				 new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    		 SecurityContextHolder.getContext().setAuthentication(authToken);
	    	 }
	     }
	     
	     filterChain.doFilter(request, response);

	}

}
