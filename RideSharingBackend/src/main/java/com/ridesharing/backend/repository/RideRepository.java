package com.ridesharing.backend.repository;

import com.ridesharing.backend.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByUserId(Long userId);
    List<Ride> findByDriverId(Long driverId);
}
