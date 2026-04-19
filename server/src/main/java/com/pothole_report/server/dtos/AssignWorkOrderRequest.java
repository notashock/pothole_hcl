package com.pothole_report.server.dtos;

import lombok.Data;

@Data
public class AssignWorkOrderRequest {
    private Long reportId;
    private Long engineerId;
}