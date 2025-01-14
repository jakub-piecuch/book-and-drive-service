package org.redcode.bookanddriveservice.trainees.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TraineeResponse(
    UUID id,
    String name,
    String sureName,
    String email
) {
    public static TraineeResponse from(Trainee trainee) {
        return TraineeResponse.builder()
            .id(trainee.getId())
            .name(trainee.getName())
            .sureName(trainee.getSureName())
            .email(trainee.getEmail())
            .build();
    }
}
