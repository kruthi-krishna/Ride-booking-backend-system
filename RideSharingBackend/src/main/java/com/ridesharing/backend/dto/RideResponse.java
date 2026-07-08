package com.ridesharing.backend.dto;

import com.ridesharing.backend.entity.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponse {
    private Long id;
    private String pickup;
    private String destination;
    private Double distance;
    private Double fare;
    private RideStatus status;
    private LocalDateTime rideTime;

    private Long userId;
    private String userName;

    private Long driverId;
    private String driverName;
    private String vehicleNumber;
}
