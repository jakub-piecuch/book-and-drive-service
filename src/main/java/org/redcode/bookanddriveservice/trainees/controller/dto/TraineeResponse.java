package org.redcode.bookanddriveservice.trainees.controller.dto;

import java.util.UUID;
import lombok.Builder;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;

@Builder
public record TraineeResponse(
    UUID id,
    String tenantId,
    String name,
    String surname,
    String email
) {
    public static TraineeResponse from(Trainee trainee) {
        return TraineeResponse.builder()
            .id(trainee.getId())
            .tenantId(trainee.getTenantId())
            .name(trainee.getName())
            .surname(trainee.getSurname())
            .email(trainee.getEmail())
            .build();
    }
}
