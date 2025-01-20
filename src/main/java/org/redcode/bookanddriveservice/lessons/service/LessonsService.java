package org.redcode.bookanddriveservice.lessons.service;

import static org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException.RESOURECE_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.exceptions.DuplicateResourceException;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
import org.redcode.bookanddriveservice.exceptions.ValidationException;
import org.redcode.bookanddriveservice.instructors.service.InstructorsService;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.redcode.bookanddriveservice.lessons.repository.LessonCustomSearchRepository;
import org.redcode.bookanddriveservice.lessons.repository.LessonSearchCriteria;
import org.redcode.bookanddriveservice.lessons.repository.LessonsRepository;
import org.redcode.bookanddriveservice.page.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonsService {

    public static final String DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT = "duplicate key value violates unique constraint";
    private final LessonsRepository lessonsRepository;
    private final LessonCustomSearchRepository lessonCustomRepository;
    private final InstructorsService instructorsService;

    public Lesson create(Lesson lesson) {
        LocalDateTime startTime = lesson.getStartTime();
        LocalDateTime endTime = lesson.getEndTime();

        if (startTime.isAfter(endTime)) {
            log.error("Start time: {}, cannot be greater than end time: {}.", startTime, endTime);
            throw ValidationException.of("Start time cannot be greater than end time", "invalid_dates");
        }

        LessonEntity lessonEntity = LessonEntity.from(lesson);
        try {
            lessonEntity = lessonsRepository.save(lessonEntity);
        } catch (Exception e) {
            if (e.getMessage().contains(DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT)) {
                throw DuplicateResourceException.of("Cannot create duplicate lesson", "duplicate_value");
            }
        }

        return Lesson.from(lessonEntity);
    }

    public PageResponse<Lesson> findByCriteria(LessonSearchCriteria criteria, PageRequest pageRequest) {
        List<Lesson> lessons = lessonCustomRepository.findAllByCriteria(criteria, pageRequest).stream()
            .map(Lesson::from)
            .toList();
        log.info("Lessons by criteria: {}", lessons);

        long lessonsCount = lessonCustomRepository.getTotalCount(criteria);
        log.info("Count lessons by criteria: {}", lessonsCount);

        var totalPages = (int) Math.ceil((double) lessonsCount / pageRequest.getPageSize());

        return PageResponse.of(lessons, new PageResponse.PageMetadata(
            pageRequest.getPageSize(),
            pageRequest.getPageNumber(),
            lessonsCount,
            totalPages
        ));
    }

    public void deleteById(UUID id) {
        lessonsRepository.findById(id)
            .ifPresentOrElse(
                lessonEntity -> lessonsRepository.deleteById(id), () -> {
                    throw ResourceNotFoundException.of(RESOURECE_NOT_FOUND);
                }
            );
    }

}
