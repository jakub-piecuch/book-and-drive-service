package org.redcode.bookanddriveservice.trainees.controller;


import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.trainees.dto.CreateTraineeRequest;
import org.redcode.bookanddriveservice.trainees.dto.Trainee;
import org.redcode.bookanddriveservice.trainees.dto.TraineeResponse;
import org.redcode.bookanddriveservice.trainees.dto.UpdateTraineeRequest;
import org.redcode.bookanddriveservice.trainees.service.TraineesService;
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
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineesController {

    private final TraineesService traineesService;

    @PostMapping
    public ResponseEntity<TraineeResponse> createTrainee(@Valid @RequestBody CreateTraineeRequest request) {
        log.info("Creating Trainee from request: {}", request);
        Trainee car = Trainee.from(request);
        Trainee savedCar = traineesService.create(car);
        TraineeResponse response = TraineeResponse.from(savedCar);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TraineeResponse>> getTrainees() {
        log.info("Fetching all trainees.");
        List<TraineeResponse> trainees = traineesService.getTrainees().stream()
            .map(TraineeResponse::from)
            .toList();

        return ResponseEntity.ok(trainees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraineeResponse> getTraineeById(@PathVariable UUID id) {
        log.info("Getting Trainee by id: {}", id);
        return Optional.ofNullable(traineesService.findById(id))
            .map(TraineeResponse::from)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TraineeResponse> updateTraineeById(@PathVariable UUID id, @RequestBody UpdateTraineeRequest request) {
        log.info("Updating Trainee by id: {}", id);
        Trainee car = Trainee.from(request);
        return Optional.ofNullable(traineesService.updateById(id, car))
            .map(TraineeResponse::from)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteTraineeById(@PathVariable UUID id) {
        log.info("Deleting Trainee by id: {}", id);
        return Optional.ofNullable(traineesService.deleteById(id))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
