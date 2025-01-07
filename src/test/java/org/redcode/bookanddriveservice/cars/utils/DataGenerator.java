package org.redcode.bookanddriveservice.cars.utils;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.model.CarEntity;

@UtilityClass
public class DataGenerator {

    private final UUID CAR_ID = UUID.randomUUID();

    public static Car generateCar() {
        return Car.builder()
            .id(CAR_ID)
            .make("Toyota")
            .model("Corolla")
            .registrationNumber("ASDQW123")
            .build();
    }

    public static CarEntity generateCarEntity() {
        return CarEntity.builder()
            .id(CAR_ID)
            .make("Toyota")
            .model("Corolla")
            .registrationNumber("ASDQW123")
            .build();
    }
}
