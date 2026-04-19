// RepairWorkOrderService.java
package com.pothole_report.server.services;

import com.pothole_report.server.enums.ReportStatus;
import com.pothole_report.server.enums.Role;
import com.pothole_report.server.enums.WorkStatus;
import com.pothole_report.server.models.PotholeReport;
import com.pothole_report.server.models.RepairWorkOrder;
import com.pothole_report.server.models.User;
import com.pothole_report.server.repositories.PotholeReportRepository;
import com.pothole_report.server.repositories.RepairWorkOrderRepository;
import com.pothole_report.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RepairWorkOrderService {

    private final RepairWorkOrderRepository workOrderRepository;
    private final PotholeReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public RepairWorkOrder assignWorkOrder(Long reportId, Long engineerId, String supervisorEmail) {
        PotholeReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (report.getReportStatus() != ReportStatus.APPROVED) {
            throw new RuntimeException("Cannot assign work order. Report is not approved.");
        }

        User engineer = userRepository.findById(engineerId)
                .orElseThrow(() -> new RuntimeException("Engineer not found"));

        if (engineer.getRole() != Role.ENGINEER) {
            throw new RuntimeException("Assigned user must be an ENGINEER.");
        }

        User supervisor = userRepository.findByEmail(supervisorEmail)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        RepairWorkOrder workOrder = new RepairWorkOrder();
        workOrder.setPotholeReport(report);
        workOrder.setAssignedTo(engineer);
        workOrder.setAssignedBy(supervisor);
        workOrder.setWorkStatus(WorkStatus.ASSIGNED);
        workOrder.setStartDate(LocalDateTime.now());

        LocalDateTime expectedCompletion = calculateSLA(report, workOrder.getStartDate());
        workOrder.setExpectedCompletionDate(expectedCompletion);

        report.setReportStatus(ReportStatus.ASSIGNED);
        reportRepository.save(report);

        return workOrderRepository.save(workOrder);
    }

    @Transactional
    public RepairWorkOrder updateWorkProgress(Long workOrderId, WorkStatus newStatus, String engineerEmail) {
        RepairWorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Work order not found"));

        if (workOrder.getWorkStatus() == WorkStatus.COMPLETED) {
            throw new RuntimeException("Cannot modify a completed work order.");
        }

        if (LocalDateTime.now().isAfter(workOrder.getExpectedCompletionDate()) && newStatus != WorkStatus.COMPLETED) {
            newStatus = WorkStatus.DELAYED;
        }

        workOrder.setWorkStatus(newStatus);
        PotholeReport report = workOrder.getPotholeReport();

        if (newStatus == WorkStatus.IN_PROGRESS) {
            report.setReportStatus(ReportStatus.IN_PROGRESS);
        } else if (newStatus == WorkStatus.COMPLETED) {
            workOrder.setActualCompletionDate(LocalDateTime.now());
            report.setReportStatus(ReportStatus.FIXED);
        }

        reportRepository.save(report);
        return workOrderRepository.save(workOrder);
    }

    private LocalDateTime calculateSLA(PotholeReport report, LocalDateTime startDate) {
        return switch (report.getSeverityLevel()) {
            case CRITICAL -> startDate.plusHours(8);
            case HIGH -> startDate.plusHours(24);
            case MEDIUM -> startDate.plusDays(3);
            case LOW -> startDate.plusDays(7);
            default -> startDate.plusDays(7);
        };
    }
}