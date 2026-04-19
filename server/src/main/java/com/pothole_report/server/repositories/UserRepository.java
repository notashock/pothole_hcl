package com.pothole_report.server.repositories;

import com.pothole_report.server.Utils.CustomUserDetails;
import com.pothole_report.server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
}
