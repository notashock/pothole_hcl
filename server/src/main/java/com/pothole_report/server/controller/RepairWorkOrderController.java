package com.pothole_report.server.controller;

import com.pothole_report.server.dtos.AssignWorkOrderRequest;
import com.pothole_report.server.dtos.UpdateWorkStatusRequest;
import com.pothole_report.server.models.RepairWorkOrder;
import com.pothole_report.server.services.RepairWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
public class RepairWorkOrderController {

    @Autowired
    private RepairWorkOrderService workOrderService;

    @PostMapping
    public ResponseEntity<?> assignWorkOrder(@RequestBody AssignWorkOrderRequest request, Authentication authentication) {
        try {
            String supervisorEmail = authentication.getName();
            RepairWorkOrder workOrder = workOrderService.assignWorkOrder(
                    request.getReportId(),
                    request.getEngineerId(),
                    supervisorEmail
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(workOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateWorkStatus(
            @PathVariable Long id,
            @RequestBody UpdateWorkStatusRequest request,
            Authentication authentication) {
        try {
            String engineerEmail = authentication.getName();
            RepairWorkOrder updatedOrder = workOrderService.updateWorkProgress(
                    id,
                    request.getWorkStatus(),
                    engineerEmail
            );
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}