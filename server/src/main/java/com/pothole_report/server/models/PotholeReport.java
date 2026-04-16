package com.pothole_report.server.models;

import com.pothole_report.server.enums.ReportStatus;
import com.pothole_report.server.enums.SeverityLevel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pothole_report")
public class PotholeReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reportedBy;

    private String description;

    @Enumerated(EnumType.STRING)
    private SeverityLevel severityLevel;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.REPORTED;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
