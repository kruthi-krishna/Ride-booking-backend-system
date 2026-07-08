package com.ridesharing.backend.controller;

import com.ridesharing.backend.dto.*;
import com.ridesharing.backend.service.DriverService;
import com.ridesharing.backend.service.RideService;
import com.ridesharing.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "Administrative oversight of users, drivers, and rides")
public class AdminController {

    private final UserService userService;
    private final DriverService driverService;
    private final RideService rideService;

    @GetMapping("/users")
    @Operation(summary = "Get all registered users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", userService.getAllUsers()));
    }

    @GetMapping("/drivers")
    @Operation(summary = "Get all registered drivers")
    public ResponseEntity<ApiResponse<List<DriverResponse>>> getAllDrivers() {
        return ResponseEntity.ok(ApiResponse.success("Drivers fetched successfully", driverService.getAllDrivers()));
    }

    @GetMapping("/rides")
    @Operation(summary = "Get all rides in the system")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getAllRides() {
        return ResponseEntity.ok(ApiResponse.success("Rides fetched successfully", rideService.getAllRides()));
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "Delete a user by id")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
}
