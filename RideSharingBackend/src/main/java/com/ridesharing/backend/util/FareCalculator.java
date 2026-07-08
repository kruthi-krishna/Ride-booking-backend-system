package com.ridesharing.backend.util;

import org.springframework.stereotype.Component;

/**
 * Utility responsible for ride fare calculation.
 *
 * Formula: fare = BASE_FARE + (distance * PER_KM_RATE)
 */
@Component
public class FareCalculator {

    private static final double BASE_FARE = 50.0;
    private static final double PER_KM_RATE = 15.0;

    public double calculateFare(double distanceInKm) {
        return BASE_FARE + (distanceInKm * PER_KM_RATE);
    }
}
