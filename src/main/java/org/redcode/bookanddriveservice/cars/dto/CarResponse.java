package org.redcode.bookanddriveservice.cars.dto;

import static java.util.Objects.isNull;

import java.util.UUID;
import lombok.Builder;

@Builder
public record CarResponse(
    UUID id,
    String make,
    String model,
    String registrationNumber
) {
    public static CarResponse from(Car car) {
        if (isNull(car)) {
            return null;
        }
        return CarResponse.builder()
            .id(car.getId())
            .make(car.getMake())
            .model(car.getModel())
            .registrationNumber(car.getRegistrationNumber())
            .build();
    }
}
