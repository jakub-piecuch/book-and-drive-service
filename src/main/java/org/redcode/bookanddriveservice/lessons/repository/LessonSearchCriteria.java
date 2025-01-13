package org.redcode.bookanddriveservice.lessons.repository;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record LessonSearchCriteria(
    LocalDateTime startTime,
    LocalDateTime endTime,
    UUID instructorId,
    UUID traineeId,
    UUID carId

) {
    public static LessonSearchCriteria from(
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID instructorId,
        UUID traineeId,
        UUID carId
    ) {
        return LessonSearchCriteria.builder()
            .startTime(startTime)
            .endTime(endTime)
            .instructorId(instructorId)
            .traineeId(traineeId)
            .carId(carId)
            .build();
    }
}
