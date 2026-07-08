package com.ridesharing.backend.service;

import com.ridesharing.backend.dto.RideRequest;
import com.ridesharing.backend.dto.RideResponse;

import java.util.List;

public interface RideService {
    RideResponse bookRide(RideRequest request);
    RideResponse acceptRide(Long rideId, Long driverId);
    RideResponse completeRide(Long rideId);
    RideResponse cancelRide(Long rideId);
    RideResponse getRideById(Long rideId);
    List<RideResponse> getRideHistory();
    List<RideResponse> getRidesByUser(Long userId);
    List<RideResponse> getRidesByDriver(Long driverId);
    List<RideResponse> getAllRides();
}
