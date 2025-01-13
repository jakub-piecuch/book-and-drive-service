package org.redcode.bookanddriveservice.lessons.controller;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.lessons.dto.LessonResponse;
import org.redcode.bookanddriveservice.lessons.repository.LessonSearchCriteria;
import org.redcode.bookanddriveservice.lessons.service.LessonsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonsController {

    private final LessonsService lessonsService;

    @PostMapping
    public ResponseEntity<Page<LessonResponse>> fetchAllByCriteria(
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
        Page<LessonResponse> response = lessonsService.findByCriteria(criteria, pageRequest)
            .map(LessonResponse::from);

        return ResponseEntity.ok(response);
    }
}
