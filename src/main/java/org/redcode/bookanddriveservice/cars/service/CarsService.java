package org.redcode.bookanddriveservice.cars.service;

import static org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException.RESOURECE_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.cars.domain.Car;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.cars.repository.CarsRepository;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
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
                CarEntity updatedCarEntity = CarEntity.update(carEntity, car);
                CarEntity savedCar = carsRepository.save(updatedCarEntity);
                return Car.from(savedCar);
            })
            .orElseThrow(() -> ResourceNotFoundException.of(RESOURECE_NOT_FOUND));
    }

    public void deleteById(UUID id) {
        carsRepository.findById(id)
            .ifPresentOrElse(
                carEntity -> carsRepository.deleteById(id), () -> {
                    throw ResourceNotFoundException.of(RESOURECE_NOT_FOUND);
                }
            );
    }

    public boolean existsById(UUID id) {
        return carsRepository.existsById(id);
    }
}
