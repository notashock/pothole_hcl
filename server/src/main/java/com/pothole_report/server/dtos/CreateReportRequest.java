package com.pothole_report.server.dtos;

import com.pothole_report.server.enums.SeverityLevel;
import lombok.Data;

@Data
public class CreateReportRequest {
    private String description;
    private SeverityLevel severityLevel;
    private String imageUrl;
    private Long locationId;
}