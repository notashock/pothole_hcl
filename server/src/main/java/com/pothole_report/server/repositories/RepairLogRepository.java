package com.pothole_report.server.repositories;

import com.pothole_report.server.models.RepairLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairLogRepository extends JpaRepository<RepairLog, Long> {
}
