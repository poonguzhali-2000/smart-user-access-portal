package com.smartportal.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartportal.backend.dto.LoginRequest;
import com.smartportal.backend.dto.LoginResponse;
import com.smartportal.backend.entity.AppUser;
import com.smartportal.backend.service.AppUserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping
    public AppUser createUser(@RequestBody AppUser appUser) {
        return appUserService.saveUser(appUser);
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }
    
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        String token =
                appUserService.login(
                        request.getEmail(),
                        request.getPassword()
                );

        return new LoginResponse(token);
    }
}