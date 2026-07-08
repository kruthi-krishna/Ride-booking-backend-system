package com.ridesharing.backend.repository;

import com.ridesharing.backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Driver> findFirstByAvailableTrue();
}
