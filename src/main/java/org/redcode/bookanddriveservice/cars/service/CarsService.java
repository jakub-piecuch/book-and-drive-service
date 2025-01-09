package org.redcode.bookanddriveservice.cars.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.cars.repository.CarsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarsRepository carsRepository;

    public Car create(Car car) {
        CarEntity carEntity = CarEntity.from(car);
        CarEntity savedCar = carsRepository.save(carEntity);

        return Car.from(savedCar);
    }

    public List<Car> getCars() {
        return carsRepository.findAll().stream()
            .map(Car::from)
            .toList();
    }

    public Car findById(UUID id) {
        return carsRepository.findById(id)
            .map(Car::from)
            .orElse(null);
    }

    public Car updateById(UUID id, Car car) {
        return carsRepository.findById(id)
            .map(carEntity -> {
                CarEntity updatedCarEntity = CarEntity.from(car);
                updatedCarEntity.setId(id);
                CarEntity savedCar = carsRepository.save(updatedCarEntity);
                return Car.from(savedCar);
            })
            .orElse(null);
    }

    public UUID deleteById(UUID id) {
        return carsRepository.findById(id)
            .map(carEntity -> {
                carsRepository.deleteById(id);
                return id;
            })
            .orElse(null);
    }

    public boolean existsById(UUID id) {
        return carsRepository.existsById(id);
    }
}
