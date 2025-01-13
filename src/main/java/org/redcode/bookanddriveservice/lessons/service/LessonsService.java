package org.redcode.bookanddriveservice.lessons.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.lessons.dto.Lesson;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.redcode.bookanddriveservice.lessons.repository.LessonCustomSearchRepository;
import org.redcode.bookanddriveservice.lessons.repository.LessonSearchCriteria;
import org.redcode.bookanddriveservice.lessons.repository.LessonsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonsService {

    private final LessonsRepository lessonsRepository;
    private final LessonCustomSearchRepository lessonCustomRepository;

    public Lesson create(Lesson lesson) {
        LocalDateTime startTime = lesson.getStartTime();
        LocalDateTime endTime = lesson.getEndTime();

        if (startTime.isAfter(endTime)) {
            log.error("Start time: {}, cannot be greater than end time: {}.", startTime, endTime);
            throw new IllegalArgumentException();
        }

        LessonEntity lessonEntity = LessonEntity.from(lesson);
        LessonEntity savedLesson = lessonsRepository.save(lessonEntity);

        return Lesson.from(savedLesson);
    }

    public Page<Lesson> findByCriteria(LessonSearchCriteria criteria, Pageable pageable) {
        List<Lesson> lessons = lessonCustomRepository.findAllByCriteria(criteria);
        log.info("Lessons by criteria: {}", lessons);

        long lessonsCount = lessonCustomRepository.getTotalCount(criteria);
        log.info("Count lessons by criteria: {}", lessonsCount);

        return new PageImpl<>(lessons, pageable, lessonsCount);
    }
}
