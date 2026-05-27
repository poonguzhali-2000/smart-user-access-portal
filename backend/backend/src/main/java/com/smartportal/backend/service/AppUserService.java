package com.smartportal.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartportal.backend.config.JwtUtil;
import com.smartportal.backend.dto.LoginResponse;
import com.smartportal.backend.dto.UserResponseDTO;
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

    public Page<UserResponseDTO> getUsers(String search, Pageable pageable) {
    	Page<AppUser> users;
        if (search == null || search.trim().isEmpty()) {
        	users = appUserRepository.findByActiveTrue(pageable);
        } else {
        	users = appUserRepository
                .findByActiveTrueAndFullNameContainingIgnoreCaseOrActiveTrueAndEmailContainingIgnoreCase(
                        search,
                        search,
                        pageable
                );
        }
        return users.map(user ->
        new UserResponseDTO(
        		user.getId(),
        		user.getFullName(),
        		user.getEmail(),
        		user.getRole()
        		)
        );
    }
    
    public LoginResponse login(String email, String password) {
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        boolean passwordMatched = passwordEncoder.matches( password, user.getPassword() );
        if (!passwordMatched) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token, user.getRole().name(), user.getEmail());
    }
    
//    public void deleteUser(Long id) {
//        appUserRepository.deleteById(id);
//    }
    
    public void deleteUser(Long id) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        appUserRepository.save(user);
    }
    
    public AppUser updateUser(Long id, AppUser updatedUser) {
        AppUser existingUser = appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        return appUserRepository.save(existingUser);
    }
}
