package org.redcode.bookanddriveservice.cars.controller;

import static org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException.RESOURECE_NOT_FOUND;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.cars.controller.dto.CarResponse;
import org.redcode.bookanddriveservice.cars.controller.dto.CreateCarRequest;
import org.redcode.bookanddriveservice.cars.controller.dto.UpdateCarRequest;
import org.redcode.bookanddriveservice.cars.domain.Car;
import org.redcode.bookanddriveservice.cars.service.CarsService;
import org.redcode.bookanddriveservice.exceptions.ErrorDetails;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
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

@Slf4j
@RestController
@Tag(name = "Cars")
@RequiredArgsConstructor
@RequestMapping("/api/cars")
class CarsController {

    private final CarsService carsService;

    @Operation(
        summary = "Create a new car",
        description = "Creates a new car with the specified make, model, and registration number",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Car created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CarResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input, missing or invalid required fields",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDetails.class)
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CreateCarRequest request) {
        Car car = Car.from(request);
        Car savedCar = carsService.create(car);
        CarResponse response = CarResponse.from(savedCar);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getCars() {
        log.info("Fetching all cars.");

        List<CarResponse> cars = carsService.getCars().stream()
            .map(CarResponse::from)
            .toList();

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable UUID id) {
        return Optional.ofNullable(carsService.findById(id))
            .map(CarResponse::from)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> ResourceNotFoundException.of(RESOURECE_NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCarById(@PathVariable UUID id, @RequestBody UpdateCarRequest request) {
        log.info("Updating Car by id: {}", id);
        Car car = Car.from(request);
        CarResponse response = CarResponse.from(carsService.updateById(id,car));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarById(@PathVariable UUID id) {
        log.info("Deleting Car by id: {}", id);
        carsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
