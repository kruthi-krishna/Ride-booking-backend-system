package com.ridesharing.backend.serviceImpl;

import com.ridesharing.backend.dto.RideRequest;
import com.ridesharing.backend.dto.RideResponse;
import com.ridesharing.backend.entity.Driver;
import com.ridesharing.backend.entity.Ride;
import com.ridesharing.backend.entity.RideStatus;
import com.ridesharing.backend.entity.User;
import com.ridesharing.backend.exception.DriverNotFoundException;
import com.ridesharing.backend.exception.InvalidRideException;
import com.ridesharing.backend.exception.RideNotFoundException;
import com.ridesharing.backend.exception.UserNotFoundException;
import com.ridesharing.backend.repository.DriverRepository;
import com.ridesharing.backend.repository.RideRepository;
import com.ridesharing.backend.repository.UserRepository;
import com.ridesharing.backend.service.RideService;
import com.ridesharing.backend.util.EntityMapper;
import com.ridesharing.backend.util.FareCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final FareCalculator fareCalculator;
    private final EntityMapper entityMapper;

    @Override
    public RideResponse bookRide(RideRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        double fare = fareCalculator.calculateFare(request.getDistance());

        Ride ride = Ride.builder()
                .pickup(request.getPickup())
                .destination(request.getDestination())
                .distance(request.getDistance())
                .fare(fare)
                .status(RideStatus.REQUESTED)
                .user(user)
                .build();

        // Automatic driver allocation: assign the first available driver, if any.
        driverRepository.findFirstByAvailableTrue().ifPresent(driver -> {
            ride.setDriver(driver);
            ride.setStatus(RideStatus.ACCEPTED);
            driver.setAvailable(false);
            driverRepository.save(driver);
        });

        Ride saved = rideRepository.save(ride);
        return entityMapper.toRideResponse(saved);
    }

    @Override
    public RideResponse acceptRide(Long rideId, Long driverId) {
        Ride ride = getRideOrThrow(rideId);
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + driverId));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideException("Ride cannot be accepted; current status is " + ride.getStatus());
        }
        if (!Boolean.TRUE.equals(driver.getAvailable())) {
            throw new InvalidRideException("Driver is not available to accept a ride");
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.ACCEPTED);
        driver.setAvailable(false);
        driverRepository.save(driver);

        Ride updated = rideRepository.save(ride);
        return entityMapper.toRideResponse(updated);
    }

    @Override
    public RideResponse completeRide(Long rideId) {
        Ride ride = getRideOrThrow(rideId);

        if (ride.getStatus() != RideStatus.ACCEPTED && ride.getStatus() != RideStatus.ONGOING) {
            throw new InvalidRideException("Ride cannot be completed; current status is " + ride.getStatus());
        }

        ride.setStatus(RideStatus.COMPLETED);

        if (ride.getDriver() != null) {
            Driver driver = ride.getDriver();
            driver.setAvailable(true);
            driverRepository.save(driver);
        }

        Ride updated = rideRepository.save(ride);
        return entityMapper.toRideResponse(updated);
    }

    @Override
    public RideResponse cancelRide(Long rideId) {
        Ride ride = getRideOrThrow(rideId);

        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideException("Ride cannot be cancelled; current status is " + ride.getStatus());
        }

        // Free up the driver if one had already been assigned.
        if (ride.getDriver() != null) {
            Driver driver = ride.getDriver();
            driver.setAvailable(true);
            driverRepository.save(driver);
        }

        ride.setStatus(RideStatus.CANCELLED);
        Ride updated = rideRepository.save(ride);
        return entityMapper.toRideResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public RideResponse getRideById(Long rideId) {
        return entityMapper.toRideResponse(getRideOrThrow(rideId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getRideHistory() {
        return getAllRides();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getRidesByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return rideRepository.findByUserId(userId)
                .stream()
                .map(entityMapper::toRideResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getRidesByDriver(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException("Driver not found with id: " + driverId);
        }
        return rideRepository.findByDriverId(driverId)
                .stream()
                .map(entityMapper::toRideResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getAllRides() {
        return rideRepository.findAll()
                .stream()
                .map(entityMapper::toRideResponse)
                .toList();
    }

    private Ride getRideOrThrow(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + rideId));
    }
}
