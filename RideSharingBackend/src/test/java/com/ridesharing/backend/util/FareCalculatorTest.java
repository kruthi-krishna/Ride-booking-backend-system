package com.ridesharing.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the FareCalculator utility.
 * fare = 50 + distance * 15
 */
class FareCalculatorTest {

    private final FareCalculator fareCalculator = new FareCalculator();

    @Test
    void calculatesFareForZeroDistance() {
        assertEquals(50.0, fareCalculator.calculateFare(0), 0.001);
    }

    @Test
    void calculatesFareForFiveKm() {
        // 50 + 5 * 15 = 125
        assertEquals(125.0, fareCalculator.calculateFare(5), 0.001);
    }

    @Test
    void calculatesFareForTenPointFiveKm() {
        // 50 + 10.5 * 15 = 207.5
        assertEquals(207.5, fareCalculator.calculateFare(10.5), 0.001);
    }
}
