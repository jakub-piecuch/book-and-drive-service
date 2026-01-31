package org.redcode.bookanddriveservice.trainees.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTraineeRequest(
    @NotNull
    String name,
    @NotNull
    String surname,
    @NotNull
    String email
) {
}
