package com.ridesharing.backend.serviceImpl;

import com.ridesharing.backend.dto.RideRequest;
import com.ridesharing.backend.dto.RideResponse;
import com.ridesharing.backend.entity.Driver;
import com.ridesharing.backend.entity.Ride;
import com.ridesharing.backend.entity.RideStatus;
import com.ridesharing.backend.entity.User;
import com.ridesharing.backend.exception.UserNotFoundException;
import com.ridesharing.backend.repository.DriverRepository;
import com.ridesharing.backend.repository.RideRepository;
import com.ridesharing.backend.repository.UserRepository;
import com.ridesharing.backend.util.EntityMapper;
import com.ridesharing.backend.util.FareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock private RideRepository rideRepository;
    @Mock private UserRepository userRepository;
    @Mock private DriverRepository driverRepository;

    private final FareCalculator fareCalculator = new FareCalculator();
    private final EntityMapper entityMapper = new EntityMapper();

    @InjectMocks
    private RideServiceImpl rideService;

    private User user;
    private Driver driver;

    @BeforeEach
    void setUp() {
        rideService = new RideServiceImpl(rideRepository, userRepository, driverRepository, fareCalculator, entityMapper);

        user = User.builder().id(1L).name("Alice").email("alice@example.com").build();
        driver = Driver.builder().id(2L).name("Bob").vehicleNumber("KA01AB1234").available(true).build();
    }

    @Test
    void bookRide_assignsAvailableDriverAndCalculatesFare() {
        RideRequest request = new RideRequest(1L, "Koramangala", "Whitefield", 10.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(driverRepository.findFirstByAvailableTrue()).thenReturn(Optional.of(driver));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride r = invocation.getArgument(0);
            r.setId(100L);
            return r;
        });

        RideResponse response = rideService.bookRide(request);

        assertEquals(RideStatus.ACCEPTED, response.getStatus());
        assertEquals(2L, response.getDriverId());
        assertEquals(200.0, response.getFare(), 0.001); // 50 + 10*15
        verify(driverRepository).save(driver);
        assertFalse(driver.getAvailable());
    }

    @Test
    void bookRide_noDriverAvailable_leavesRideRequested() {
        RideRequest request = new RideRequest(1L, "Koramangala", "Whitefield", 4.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(driverRepository.findFirstByAvailableTrue()).thenReturn(Optional.empty());
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response = rideService.bookRide(request);

        assertEquals(RideStatus.REQUESTED, response.getStatus());
        assertNull(response.getDriverId());
        assertEquals(110.0, response.getFare(), 0.001); // 50 + 4*15
    }

    @Test
    void bookRide_userNotFound_throwsException() {
        RideRequest request = new RideRequest(99L, "A", "B", 5.0);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> rideService.bookRide(request));
    }
}
