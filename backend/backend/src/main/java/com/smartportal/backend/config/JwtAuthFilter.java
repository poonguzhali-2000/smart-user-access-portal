package com.smartportal.backend.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartportal.backend.entity.AppUser;
import com.smartportal.backend.repository.AppUserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

    	String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;
        
        try {
        	if (authHeader != null && authHeader.startsWith("Bearer ")) {
        		token = authHeader.substring(7);
        		email = jwtUtil.extractEmail(token);
        		}
        	
        	if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        		AppUser user = appUserRepository.findByEmail(email).orElse(null);
        		
        		if (user != null && jwtUtil.validateToken(token, user.getEmail())) {
        			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        			
        			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        				    new User(
        				        user.getEmail(),
        				        user.getPassword(),
        				        java.util.Collections.singletonList(authority)
        				    ),
        				    null,
        				    java.util.Collections.singletonList(authority)
        				);
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
        		 }
            }
        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT token is invalid or expired. Please log in again."
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return HttpMethod.OPTIONS.matches(request.getMethod())
                || (
                        "/api/users/login".equals(request.getServletPath())
                                && HttpMethod.POST.matches(request.getMethod())
                );
    }
}