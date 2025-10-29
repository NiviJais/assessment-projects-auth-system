package com.cloudamize.auth_system.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cloudamize.auth_system.auth.JwtUtil;
import com.cloudamize.auth_system.entity.User;
import com.cloudamize.auth_system.service.CustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	private final CustomUserDetailsService userService;
	private final JwtUtil jwtUtil;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		DefaultOAuth2User  outh2User = (DefaultOAuth2User) authentication.getPrincipal();
		String email = outh2User.getAttribute("email");
		String sub = outh2User.getAttribute("sub");
		String picture = outh2User.getAttribute("picture");
		
		log.info("Google login access for  {} :", email);

        User user = userService.saveGoogleUser(email, sub, picture);

        //Generate JWT
        String token = jwtUtil.generateToken(email);
        
        log.info("token : {}", token);
        //Redirect to frontend or show token
        response.sendRedirect("/auth/success?token=" + token);
		
	}

}
