package com.pothole_report.server.repositories;

import com.pothole_report.server.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
