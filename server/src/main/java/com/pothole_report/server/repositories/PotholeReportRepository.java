package com.pothole_report.server.repositories;

import com.pothole_report.server.models.PotholeReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotholeReportRepository extends JpaRepository<PotholeReport, Long> {
}
