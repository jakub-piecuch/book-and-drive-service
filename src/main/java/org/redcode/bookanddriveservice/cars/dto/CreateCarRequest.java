package org.redcode.bookanddriveservice.cars.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCarRequest(
    @NotNull
    String make,
    @NotNull
    String model,
    @NotNull
    String registrationNumber
) {

}
