package com.smartportal.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartportal.backend.config.JwtUtil;
import com.smartportal.backend.dto.LoginResponse;
import com.smartportal.backend.entity.AppUser;
import com.smartportal.backend.repository.AppUserRepository;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

//    public AppUser saveUser(AppUser appUser) {
//        return appUserRepository.save(appUser);
//    }
    
    public AppUser saveUser(AppUser appUser) {
        appUser.setPassword(
            passwordEncoder.encode(appUser.getPassword())
        );
        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }
    
    public LoginResponse login(String email, String password) {
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        boolean passwordMatched = passwordEncoder.matches( password, user.getPassword() );
        if (!passwordMatched) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token, user.getRole().name());
    	}
	}
