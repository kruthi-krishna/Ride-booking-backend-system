package com.ridesharing.backend.controller;

import com.ridesharing.backend.dto.*;
import com.ridesharing.backend.service.DriverService;
import com.ridesharing.backend.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver APIs", description = "Registration, login, availability status, and ride history for drivers")
public class DriverController {

    private final DriverService driverService;
    private final RideService rideService;

    @PostMapping("/register")
    @Operation(summary = "Register a new driver")
    public ResponseEntity<ApiResponse<DriverResponse>> register(@Valid @RequestBody DriverRegisterRequest request) {
        DriverResponse response = driverService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Driver registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login as a driver")
    public ResponseEntity<ApiResponse<DriverResponse>> login(@Valid @RequestBody LoginRequest request) {
        DriverResponse response = driverService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update driver availability status")
    public ResponseEntity<ApiResponse<DriverResponse>> updateStatus(@PathVariable Long id,
                                                                      @Valid @RequestBody DriverStatusUpdateRequest request) {
        DriverResponse response = driverService.updateAvailability(id, request);
        return ResponseEntity.ok(ApiResponse.success("Driver status updated successfully", response));
    }

    @GetMapping("/{id}/rides")
    @Operation(summary = "Get all rides handled by a driver")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getDriverRides(@PathVariable Long id) {
        List<RideResponse> rides = rideService.getRidesByDriver(id);
        return ResponseEntity.ok(ApiResponse.success("Rides fetched successfully", rides));
    }
}
