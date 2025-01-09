package org.redcode.bookanddriveservice.lessons.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateLessonRequest(
    @NotNull
    LocalDateTime startTime,
    @NotNull
    LocalDateTime endTime,
    @NotNull
    UUID instructorId,
    @NotNull
    UUID traineeId,
    UUID carId
){
}
