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
import org.redcode.bookanddriveservice.notifications.service.LessonNotificationService;
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
    private final LessonNotificationService lessonNotificationService;

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

        Lesson created = Lesson.from(lessonEntity);
        try {
            lessonNotificationService.sendLessonScheduledEmail(created, created.getTrainee());
        } catch (Exception e) {
            log.error("Failed to send lesson scheduled email [lessonId={}, traineeId={}]: {}",
                created.getId(), created.getTrainee().getId(), e.getMessage());
        }
        return created;
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
            pageRequest.getPageNumber(),
            pageRequest.getPageSize(),
            lessonsCount,
            totalPages
        ));
    }

    public Lesson updateById(UUID id, Lesson lesson) {
        if (lesson.getStartTime().isAfter(lesson.getEndTime())) {
            log.error("Start time: {}, cannot be greater than end time: {}.", lesson.getStartTime(), lesson.getEndTime());
            throw ValidationException.of("Start time cannot be greater than end time", "invalid_dates");
        }

        return lessonsRepository.findById(id)
            .map(entity -> {
                LessonEntity updated = LessonEntity.update(entity, lesson);
                return Lesson.from(lessonsRepository.save(updated));
            })
            .orElseThrow(() -> ResourceNotFoundException.of(RESOURECE_NOT_FOUND));
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
