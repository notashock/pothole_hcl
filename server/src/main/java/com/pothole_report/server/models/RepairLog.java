package com.pothole_report.server.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "repair_logs")
public class RepairLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "work_order_id")
    private RepairWorkOrder repairWorkOrder;

    private String updateDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
