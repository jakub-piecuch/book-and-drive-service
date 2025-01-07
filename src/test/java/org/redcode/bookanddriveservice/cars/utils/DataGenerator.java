package org.redcode.bookanddriveservice.cars.utils;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.model.CarEntity;

@UtilityClass
public class DataGenerator {

    private static final String MAKE = "Toyota";
    private static final UUID CAR_ID = UUID.randomUUID();
    private static final String MODEL = "Corolla";
    public static final String REGISTRATION_NUMBER = "ASDQW123";

    public static Car generateCar() {
        return Car.builder()
            .id(CAR_ID)
            .make(MAKE)
            .model(MODEL)
            .registrationNumber(REGISTRATION_NUMBER)
            .build();
    }

    public static CarEntity generateCarEntity() {
        return CarEntity.builder()
            .id(CAR_ID)
            .make(MAKE)
            .model(MODEL)
            .registrationNumber(REGISTRATION_NUMBER)
            .build();
    }
}
