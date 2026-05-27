package com.smartportal.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartportal.backend.entity.AccessRecord;

public interface AccessRecordRepository
        extends JpaRepository<AccessRecord, Long> {

    List<AccessRecord> findByAssignedTo(String assignedTo);
}