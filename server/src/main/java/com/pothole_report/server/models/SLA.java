package com.pothole_report.server.models;

import com.pothole_report.server.enums.SeverityLevel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "service_level_agreement")
public class SLA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private SeverityLevel severityLevel;
    private int resTimeHrs;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
