package org.redcode.bookanddriveservice.instructors.controller.dto;

import java.util.UUID;
import lombok.Builder;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;

@Builder
public record InstructorResponse(
    UUID id,
    String tenantId,
    String name,
    String surname,
    String email,
    String phoneNumber
) {
    public static InstructorResponse from(Instructor instructor) {
        return InstructorResponse.builder()
            .id(instructor.getId())
            .tenantId(instructor.getTenantId())
            .name(instructor.getName())
            .surname(instructor.getSurname())
            .email(instructor.getEmail())
            .phoneNumber(instructor.getPhoneNumber())
            .build();
    }
}
