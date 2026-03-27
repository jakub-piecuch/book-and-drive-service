package org.redcode.bookanddriveservice.instructors.controller.dto;

import lombok.Builder;

@Builder
public record UpdateInstructorRequest(
    String name,
    String surname,
    String email,
    String phoneNumber
) {
}
