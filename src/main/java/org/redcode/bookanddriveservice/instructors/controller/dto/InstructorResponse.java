package org.redcode.bookanddriveservice.instructors.controller.dto;

import java.util.UUID;
import lombok.Builder;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;

@Builder
public record InstructorResponse(
    UUID id,
    String name,
    String sureName,
    String email
) {
    public static InstructorResponse from(Instructor instructor) {
        return InstructorResponse.builder()
            .id(instructor.getId())
            .name(instructor.getName())
            .sureName(instructor.getSureName())
            .email(instructor.getEmail())
            .build();
    }
}
