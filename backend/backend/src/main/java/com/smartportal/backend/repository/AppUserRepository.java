package com.smartportal.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartportal.backend.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
    
    Page<AppUser> findByActiveTrue(Pageable pageable);

    @Query("""
    		SELECT u FROM AppUser u
    		WHERE u.active = true
    		AND (
    		LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
    		OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
    		)
    """)
    Page<AppUser> searchUsers(@Param("search") String search,Pageable pageable);
}