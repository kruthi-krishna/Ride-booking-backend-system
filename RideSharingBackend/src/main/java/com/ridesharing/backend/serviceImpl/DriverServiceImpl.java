package com.ridesharing.backend.serviceImpl;

import com.ridesharing.backend.dto.DriverRegisterRequest;
import com.ridesharing.backend.dto.DriverResponse;
import com.ridesharing.backend.dto.DriverStatusUpdateRequest;
import com.ridesharing.backend.dto.LoginRequest;
import com.ridesharing.backend.entity.Driver;
import com.ridesharing.backend.exception.DriverNotFoundException;
import com.ridesharing.backend.exception.EmailAlreadyExistsException;
import com.ridesharing.backend.exception.InvalidCredentialsException;
import com.ridesharing.backend.repository.DriverRepository;
import com.ridesharing.backend.service.DriverService;
import com.ridesharing.backend.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    @Override
    public DriverResponse register(DriverRegisterRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("A driver with email '" + request.getEmail() + "' already exists");
        }

        Driver driver = Driver.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .vehicleNumber(request.getVehicleNumber())
                .vehicleType(request.getVehicleType())
                .available(true)
                .rating(5.0)
                .build();

        Driver saved = driverRepository.save(driver);
        return entityMapper.toDriverResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse login(LoginRequest request) {
        Driver driver = driverRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), driver.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return entityMapper.toDriverResponse(driver);
    }

    @Override
    public DriverResponse updateAvailability(Long driverId, DriverStatusUpdateRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + driverId));

        driver.setAvailable(request.getAvailable());
        Driver updated = driverRepository.save(driver);
        return entityMapper.toDriverResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(entityMapper::toDriverResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findAvailableDriverId() {
        return driverRepository.findFirstByAvailableTrue()
                .map(Driver::getId)
                .orElse(null);
    }
}
