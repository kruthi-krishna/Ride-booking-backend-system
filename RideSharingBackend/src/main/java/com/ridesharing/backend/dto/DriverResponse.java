package com.ridesharing.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String vehicleNumber;
    private String vehicleType;
    private Boolean available;
    private Double rating;
}
