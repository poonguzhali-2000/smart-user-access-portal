package com.smartportal.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartportal.backend.entity.AccessRecord;
import com.smartportal.backend.repository.AccessRecordRepository;

@Service
public class AccessRecordService {

    @Autowired
    private AccessRecordRepository accessRecordRepository;

    public List<AccessRecord> getAllRecords() {
        return accessRecordRepository.findAll();
    }

    public List<AccessRecord> getUserRecords(
            String email
    ) {
        return accessRecordRepository
                .findByAssignedTo(email);
    }
}