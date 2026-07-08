package com.ridesharing.backend.service;

import com.ridesharing.backend.dto.LoginRequest;
import com.ridesharing.backend.dto.UserRegisterRequest;
import com.ridesharing.backend.dto.UserResponse;
import com.ridesharing.backend.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse login(LoginRequest request);
    UserResponse getProfile(Long userId);
    UserResponse updateProfile(Long userId, UserUpdateRequest request);
    List<UserResponse> getAllUsers();
    void deleteUser(Long userId);
}
