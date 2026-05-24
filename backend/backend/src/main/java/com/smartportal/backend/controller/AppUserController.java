package com.smartportal.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}