package com.ridesharing.backend.util;

import com.ridesharing.backend.dto.*;
import com.ridesharing.backend.entity.Driver;
import com.ridesharing.backend.entity.Ride;
import com.ridesharing.backend.entity.User;
import org.springframework.stereotype.Component;

/**
 * Central place for mapping entities to their corresponding response DTOs.
 */
@Component
public class EntityMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public DriverResponse toDriverResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .name(driver.getName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .vehicleNumber(driver.getVehicleNumber())
                .vehicleType(driver.getVehicleType())
                .available(driver.getAvailable())
                .rating(driver.getRating())
                .build();
    }

    public RideResponse toRideResponse(Ride ride) {
        RideResponse.RideResponseBuilder builder = RideResponse.builder()
                .id(ride.getId())
                .pickup(ride.getPickup())
                .destination(ride.getDestination())
                .distance(ride.getDistance())
                .fare(ride.getFare())
                .status(ride.getStatus())
                .rideTime(ride.getRideTime());

        if (ride.getUser() != null) {
            builder.userId(ride.getUser().getId())
                   .userName(ride.getUser().getName());
        }

        if (ride.getDriver() != null) {
            builder.driverId(ride.getDriver().getId())
                   .driverName(ride.getDriver().getName())
                   .vehicleNumber(ride.getDriver().getVehicleNumber());
        }

        return builder.build();
    }
}
