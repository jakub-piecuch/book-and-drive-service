package org.redcode.bookanddriveservice.cars.mappers;

import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.dto.CarResponse;
import org.redcode.bookanddriveservice.cars.dto.CreateCarRequest;
import org.redcode.bookanddriveservice.cars.dto.UpdateCarRequest;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.springframework.stereotype.Component;

@Component
public class CarsMapper {

    public Car mapToCar(CarEntity carEntity) {
        return Car.builder()
            .id(carEntity.getId())
            .make(carEntity.getMake())
            .model(carEntity.getModel())
            .registrationNumber(carEntity.getRegistrationNumber())
            .build();
    }

    public Car mapToCar(CreateCarRequest request) {
        return Car.builder()
            .make(request.make())
            .model(request.model())
            .registrationNumber(request.registrationNumber())
            .build();
    }

    public Car mapToCar(UpdateCarRequest request) {
        return Car.builder()
            .make(request.make())
            .model(request.model())
            .registrationNumber(request.registrationNumber())
            .build();
    }

    public CarEntity mapToCarEntity(Car car) {
        return CarEntity.builder()
            .id(car.getId())
            .make(car.getMake())
            .model(car.getModel())
            .registrationNumber(car.getRegistrationNumber())
            .build();
    }

    public CarResponse mapToCarResponse(Car car) {
        return CarResponse.builder()
            .id(car.getId())
            .make(car.getMake())
            .model(car.getModel())
            .registrationNumber(car.getRegistrationNumber())
            .build();
    }
}
