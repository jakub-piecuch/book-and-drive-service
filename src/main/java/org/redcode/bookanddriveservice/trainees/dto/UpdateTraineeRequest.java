package org.redcode.bookanddriveservice.trainees.dto;

import lombok.Builder;

@Builder
public record UpdateTraineeRequest(
    String name,
    String sureName,
    String email
) {
}
