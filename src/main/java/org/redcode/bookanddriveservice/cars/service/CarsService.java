package org.redcode.bookanddriveservice.cars.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.mappers.CarsMapper;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.cars.repository.CarsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarsRepository carsRepository;
    private final CarsMapper carsMapper;

    public Car create(Car car) {
        CarEntity carEntity = carsMapper.mapToCarEntity(car);
        CarEntity savedCar = carsRepository.save(carEntity);

        return carsMapper.mapToCar(savedCar);
    }

    public List<Car> getCars() {
        return carsRepository.findAll().stream()
            .map(carsMapper::mapToCar)
            .toList();
    }

    public Car findById(UUID id) {
        return carsRepository.findById(id)
            .map(carsMapper::mapToCar)
            .orElse(null);
    }

    public Car updateById(UUID id, Car car) {
        return carsRepository.findById(id)
            .map(carEntity -> {
                CarEntity updatedCarEntity = carsMapper.mapToCarEntity(car);
                updatedCarEntity.setId(id);
                CarEntity savedCar = carsRepository.save(updatedCarEntity);
                return carsMapper.mapToCar(savedCar);
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
