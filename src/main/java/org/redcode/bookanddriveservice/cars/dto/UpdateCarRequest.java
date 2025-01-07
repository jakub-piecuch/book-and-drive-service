package org.redcode.bookanddriveservice.cars.dto;

public record UpdateCarRequest(
    String make,
    String model,
    String registrationNumber
) {
}
