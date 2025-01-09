package org.redcode.bookanddriveservice.cars.dto;

import lombok.Builder;

@Builder
public record UpdateCarRequest(
    String make,
    String model,
    String registrationNumber
) {
}
