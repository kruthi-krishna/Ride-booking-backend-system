package com.ridesharing.backend.controller;

import com.ridesharing.backend.dto.ApiResponse;
import com.ridesharing.backend.dto.RideRequest;
import com.ridesharing.backend.dto.RideResponse;
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
@RequestMapping("/rides")
@RequiredArgsConstructor
@Tag(name = "Ride APIs", description = "Ride booking, driver acceptance, completion, cancellation, and history")
public class RideController {

    private final RideService rideService;

    @PostMapping("/book")
    @Operation(summary = "Book a new ride (auto-assigns the first available driver, if any)")
    public ResponseEntity<ApiResponse<RideResponse>> bookRide(@Valid @RequestBody RideRequest request) {
        RideResponse response = rideService.bookRide(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ride booked successfully", response));
    }

    @PutMapping("/{id}/accept")
    @Operation(summary = "Driver accepts a requested ride")
    public ResponseEntity<ApiResponse<RideResponse>> acceptRide(@PathVariable Long id,
                                                                  @RequestParam Long driverId) {
        RideResponse response = rideService.acceptRide(id, driverId);
        return ResponseEntity.ok(ApiResponse.success("Ride accepted successfully", response));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark a ride as completed")
    public ResponseEntity<ApiResponse<RideResponse>> completeRide(@PathVariable Long id) {
        RideResponse response = rideService.completeRide(id);
        return ResponseEntity.ok(ApiResponse.success("Ride completed successfully", response));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a ride")
    public ResponseEntity<ApiResponse<RideResponse>> cancelRide(@PathVariable Long id) {
        RideResponse response = rideService.cancelRide(id);
        return ResponseEntity.ok(ApiResponse.success("Ride cancelled successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride details by id")
    public ResponseEntity<ApiResponse<RideResponse>> getRide(@PathVariable Long id) {
        RideResponse response = rideService.getRideById(id);
        return ResponseEntity.ok(ApiResponse.success("Ride fetched successfully", response));
    }

    @GetMapping("/history")
    @Operation(summary = "Get complete ride history")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getRideHistory() {
        List<RideResponse> rides = rideService.getRideHistory();
        return ResponseEntity.ok(ApiResponse.success("Ride history fetched successfully", rides));
    }
}
