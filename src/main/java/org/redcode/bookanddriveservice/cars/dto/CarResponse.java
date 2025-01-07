package org.redcode.bookanddriveservice.cars.dto;

import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CarResponse(
    UUID id,
    String make,
    String model,
    String registrationNumber
) {
}
