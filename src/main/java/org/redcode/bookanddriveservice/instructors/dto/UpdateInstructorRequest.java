package org.redcode.bookanddriveservice.instructors.dto;

import lombok.Builder;

@Builder
public record UpdateInstructorRequest(
    String name,
    String sureName,
    String email
) {
}
