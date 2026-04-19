// PotholeReportService.java
package com.pothole_report.server.services;

import com.pothole_report.server.enums.ReportStatus;
import com.pothole_report.server.enums.Role;
import com.pothole_report.server.models.Location;
import com.pothole_report.server.models.PotholeReport;
import com.pothole_report.server.models.User;
import com.pothole_report.server.repositories.LocationRepository;
import com.pothole_report.server.repositories.PotholeReportRepository;
import com.pothole_report.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PotholeReportService {

    private final PotholeReportRepository reportRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public PotholeReport createReport(String email, PotholeReport reportDto, Long locationId) {
        User citizen = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (citizen.getRole() != Role.CITIZEN) {
            throw new RuntimeException("Only citizens can report potholes.");
        }

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Invalid location provided."));

        PotholeReport report = new PotholeReport();
        report.setReportedBy(citizen);
        report.setLocation(location);
        report.setDescription(reportDto.getDescription());
        report.setSeverityLevel(reportDto.getSeverityLevel());
        report.setImageUrl(reportDto.getImageUrl());
        report.setReportStatus(ReportStatus.REPORTED);

        return reportRepository.save(report);
    }

    public List<PotholeReport> getAllReports() {
        return reportRepository.findAll();
    }

    public PotholeReport getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Transactional
    public PotholeReport approveReport(Long reportId) {
        PotholeReport report = getReportById(reportId);

        if (report.getReportStatus() != ReportStatus.REPORTED && report.getReportStatus() != ReportStatus.UNDER_REVIEW) {
            throw new RuntimeException("Only newly reported or under-review complaints can be approved.");
        }

        report.setReportStatus(ReportStatus.APPROVED);
        return reportRepository.save(report);
    }

    @Transactional
    public PotholeReport rejectReport(Long reportId) {
        PotholeReport report = getReportById(reportId);

        if (report.getReportStatus() == ReportStatus.REJECTED) {
            throw new RuntimeException("Report is already rejected.");
        }

        report.setReportStatus(ReportStatus.REJECTED);
        return reportRepository.save(report);
    }
}