package org.redcode.bookanddriveservice.trainees.controller.dto;

import lombok.Builder;

@Builder
public record UpdateTraineeRequest(
    String name,
    String surname,
    String email,
    String phoneNumber
) {
}
