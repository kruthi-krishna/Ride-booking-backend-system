package com.ridesharing.backend.exception;

public class InvalidRideException extends RuntimeException {
    public InvalidRideException(String message) {
        super(message);
    }
}
