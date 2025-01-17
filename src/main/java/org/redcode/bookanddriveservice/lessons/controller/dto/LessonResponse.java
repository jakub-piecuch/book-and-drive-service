package org.redcode.bookanddriveservice.lessons.controller.dto;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;

@Builder
public record LessonResponse(
    UUID id,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime startTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
