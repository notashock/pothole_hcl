package com.pothole_report.server.dtos;

import com.pothole_report.server.enums.WorkStatus;
import lombok.Data;

@Data
public class UpdateWorkStatusRequest {
    private WorkStatus workStatus;
}