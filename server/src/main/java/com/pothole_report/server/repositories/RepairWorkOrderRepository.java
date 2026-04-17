package com.pothole_report.server.repositories;

import com.pothole_report.server.models.RepairWorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairWorkOrderRepository extends JpaRepository<RepairWorkOrder, Long> {
}
