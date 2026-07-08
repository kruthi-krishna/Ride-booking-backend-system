package com.ridesharing.backend.service;

import com.ridesharing.backend.dto.DriverRegisterRequest;
import com.ridesharing.backend.dto.DriverResponse;
import com.ridesharing.backend.dto.DriverStatusUpdateRequest;
import com.ridesharing.backend.dto.LoginRequest;

import java.util.List;

public interface DriverService {
    DriverResponse register(DriverRegisterRequest request);
    DriverResponse login(LoginRequest request);
    DriverResponse updateAvailability(Long driverId, DriverStatusUpdateRequest request);
    List<DriverResponse> getAllDrivers();
    Long findAvailableDriverId();
}
