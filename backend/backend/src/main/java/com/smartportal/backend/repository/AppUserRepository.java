package com.smartportal.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartportal.backend.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
    
    Page<AppUser> findByActiveTrue(Pageable pageable);

    Page<AppUser> findByActiveTrueAndFullNameContainingIgnoreCaseOrActiveTrueAndEmailContainingIgnoreCase(
            String fullName,
            String email,
            Pageable pageable
    );

}