package org.redcode.bookanddriveservice.lessons.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.lessons.controller.dto.CreateLessonRequest;
import org.redcode.bookanddriveservice.lessons.controller.dto.LessonResponse;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.lessons.repository.LessonSearchCriteria;
import org.redcode.bookanddriveservice.lessons.service.LessonsService;
import org.redcode.bookanddriveservice.page.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonsController {

    private final LessonsService lessonsService;

    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(@Valid @RequestBody CreateLessonRequest request) {
        log.info("Create lesson from request: {}", request);
        Lesson lesson = Lesson.from(request);
        Lesson createdLesson = lessonsService.create(lesson);
        LessonResponse response = LessonResponse.from(createdLesson);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<LessonResponse>> fetchAllByCriteria(
        @RequestParam (required = false) LocalDateTime startDateTime,
        @RequestParam (required = false) LocalDateTime endDateTime,
        @RequestParam (required = false) UUID instructorId,
        @RequestParam (required = false) UUID traineeId,
        @RequestParam (required = false) UUID carId,
        @RequestParam (defaultValue = "0") Integer page,
        @RequestParam (defaultValue = "10") Integer limit
    ) {
        LessonSearchCriteria criteria = LessonSearchCriteria.from(
            startDateTime, endDateTime, instructorId, traineeId, carId
        );

        PageRequest pageRequest = PageRequest.of(page, limit);

        log.info("Fetching all Lessons by criteria: {}.", criteria);
        PageResponse<Lesson> result = lessonsService.findByCriteria(criteria, pageRequest);

        return ResponseEntity.ok(PageResponse.from(result, LessonResponse::from));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteLessonById(@PathVariable UUID id) {
        log.info("Deleting Lesson by id: {}", id);
        lessonsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
