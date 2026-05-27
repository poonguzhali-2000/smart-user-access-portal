package com.smartportal.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.smartportal.backend.entity.AccessRecord;
import com.smartportal.backend.service.AccessRecordService;

@RestController
@RequestMapping("/api/records")
@CrossOrigin("*")
public class AccessRecordController {

    @Autowired
    private AccessRecordService accessRecordService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<AccessRecord> getAllRecords() {
        return accessRecordService.getAllRecords();
    }

    @GetMapping("/my")
    public List<AccessRecord> getMyRecords(
            Principal principal
    ) {
        return accessRecordService.getUserRecords(
                principal.getName()
        );
    }
}