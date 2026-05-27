package com.smartportal.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartportal.backend.dto.LoginRequest;
import com.smartportal.backend.dto.LoginResponse;
import com.smartportal.backend.dto.UserResponseDTO;
import com.smartportal.backend.entity.AppUser;
import com.smartportal.backend.service.AppUserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/signup")
    public AppUser createUser(@RequestBody AppUser appUser){
        return appUserService.saveUser(appUser); 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserResponseDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") long delay
    ) throws InterruptedException {

        if (delay > 0) {
            Thread.sleep(delay);
        }

        return appUserService.getUsers(search, PageRequest.of(page, size, Sort.by("id").ascending()));
    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return appUserService.login(request.getEmail(), request.getPassword());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public AppUser updateUser(@PathVariable Long id, @RequestBody AppUser updatedUser) {
        return appUserService.updateUser(id, updatedUser);
    }
}