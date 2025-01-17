package org.redcode.bookanddriveservice.instructors.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.instructors.controller.dto.CreateInstructorRequest;
import org.redcode.bookanddriveservice.instructors.controller.dto.InstructorResponse;
import org.redcode.bookanddriveservice.instructors.controller.dto.UpdateInstructorRequest;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.instructors.service.InstructorsService;
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
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorsController {

    private final InstructorsService instructorsService;

    @PostMapping
    public ResponseEntity<InstructorResponse> createInstructors(@Valid @RequestBody CreateInstructorRequest request) {
        log.info("Creating Instructor from request: {}", request);
        Instructor car = Instructor.from(request);
        Instructor savedCar = instructorsService.create(car);
        InstructorResponse response = InstructorResponse.from(savedCar);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InstructorResponse>> getInstructors() {
        log.info("Fetching all instructors.");
        List<InstructorResponse> cars = instructorsService.getInstructors().stream()
            .map(InstructorResponse::from)
            .toList();

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponse> getInstructorById(@PathVariable UUID id) {
        log.info("Getting Instructor by id: {}", id);
        return Optional.ofNullable(instructorsService.findById(id))
            .map(InstructorResponse::from)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponse> updateInstructorById(@PathVariable UUID id, @RequestBody UpdateInstructorRequest request) {
        log.info("Updating Instructor by id: {}", id);
        Instructor car = Instructor.from(request);
        InstructorResponse response = InstructorResponse.from(instructorsService.updateById(id, car));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteInstructorById(@PathVariable UUID id) {
        log.info("Deleting Instructor by id: {}", id);
        instructorsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

