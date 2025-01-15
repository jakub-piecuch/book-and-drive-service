package org.redcode.bookanddriveservice.lessons.controller.dto;

import jakarta.annotation.Nullable;
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
    @Nullable
    UUID carId
){
}
