package com.pothole_report.server.controller;

import com.pothole_report.server.dtos.CreateReportRequest;
import com.pothole_report.server.models.PotholeReport;
import com.pothole_report.server.services.PotholeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class PotholeReportController {

    @Autowired
    private PotholeReportService reportService;

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody CreateReportRequest request, Authentication authentication) {
        try {
            PotholeReport reportDto = new PotholeReport();
            reportDto.setDescription(request.getDescription());
            reportDto.setSeverityLevel(request.getSeverityLevel());
            reportDto.setImageUrl(request.getImageUrl());

            String userEmail = authentication.getName();
            PotholeReport createdReport = reportService.createReport(userEmail, reportDto, request.getLocationId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PotholeReport>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reportService.getReportById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveReport(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reportService.approveReport(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectReport(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reportService.rejectReport(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}