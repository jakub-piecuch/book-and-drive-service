package org.redcode.bookanddriveservice.cars.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.dto.CarResponse;
import org.redcode.bookanddriveservice.cars.dto.CreateCarRequest;
import org.redcode.bookanddriveservice.cars.dto.UpdateCarRequest;
import org.redcode.bookanddriveservice.cars.mappers.CarsMapper;
import org.redcode.bookanddriveservice.cars.service.CarsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
class CarsController {

    private final CarsService carsService;
    private final CarsMapper carMapper;

    CarsController(CarsService carsService, CarsMapper carMapper) {
        this.carsService = carsService;
        this.carMapper = carMapper;
    }

    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CreateCarRequest request) {
        Car car = carMapper.mapToCar(request);
        Car savedCar = carsService.create(car);
        CarResponse response = carMapper.mapToCarResponse(savedCar);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getCars() {
        List<CarResponse> cars = carsService.getCars().stream()
            .map(carMapper::mapToCarResponse)
            .toList();

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable UUID id) {
        return Optional.ofNullable(carsService.findById(id))
            .map(carMapper::mapToCarResponse)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCarById(@PathVariable UUID id, @RequestBody UpdateCarRequest request) {
        var car = carMapper.mapToCar(request);
        return Optional.ofNullable(carsService.updateById(id, car))
            .map(carMapper::mapToCarResponse)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteCarById(@PathVariable UUID id) {
        return Optional.ofNullable(carsService.deleteById(id))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
