package com.ridesharing.backend.controller;

import com.ridesharing.backend.dto.*;
import com.ridesharing.backend.service.RideService;
import com.ridesharing.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User APIs", description = "Registration, login, profile management, and ride history for riders")
public class UserController {

    private final UserService userService;
    private final RideService rideService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login as a user")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request) {
        UserResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get user profile by id")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@PathVariable Long id) {
        UserResponse response = userService.getProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", response));
    }

    @PutMapping("/profile/{id}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@PathVariable Long id,
                                                                     @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @GetMapping("/{id}/rides")
    @Operation(summary = "Get all rides booked by a user")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getUserRides(@PathVariable Long id) {
        List<RideResponse> rides = rideService.getRidesByUser(id);
        return ResponseEntity.ok(ApiResponse.success("Rides fetched successfully", rides));
    }
}
