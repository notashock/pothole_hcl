package com.pothole_report.server.models;

import com.pothole_report.server.enums.WorkStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "repairWorkOrder")
public class RepairWorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "report_id")
    @OneToOne
    private PotholeReport potholeReport;

    @OneToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    private LocalDateTime startDate;
    private LocalDateTime expectedCompletionDate;
    private LocalDateTime actualCompletionDate;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
