package org.redcode.bookanddriveservice.lessons.dto;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record LessonResponse(
    UUID id,
    LocalDateTime startTime,
    LocalDateTime endTime,
    UUID instructorId,
    UUID traineeId,
    UUID carId
) {
    public static LessonResponse from(Lesson lesson) {
        LessonResponseBuilder lessonResponseBuilder = LessonResponse.builder()
            .id(lesson.getId())
            .startTime(lesson.getStartTime())
            .endTime(lesson.getEndTime())
            .instructorId((lesson.getInstructor().getId()))
            .traineeId(lesson.getTrainee().getId());

        if (nonNull(lesson.getCar())) {
            lessonResponseBuilder.carId(lesson.getCar().getId());
        }

        return lessonResponseBuilder.build();

    }
}
