package org.redcode.bookanddriveservice.cars.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.redcode.bookanddriveservice.cars.model.CarEntity;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Car {
    private UUID id;
    private String make;
    private String model;
    private String registrationNumber;

    public static Car from(CarEntity car) {
        return Car.builder()
            .id(car.getId())
            .make(car.getMake())
            .model(car.getModel())
            .registrationNumber(car.getRegistrationNumber())
            .build();
    }

    public static Car from(CreateCarRequest request) {
        return Car.builder()
            .make(request.make())
            .model(request.model())
            .registrationNumber(request.registrationNumber())
            .build();
    }

    public static Car from(UpdateCarRequest request) {
        return Car.builder()
            .make(request.make())
            .model(request.model())
            .registrationNumber(request.registrationNumber())
            .build();
    }
}
