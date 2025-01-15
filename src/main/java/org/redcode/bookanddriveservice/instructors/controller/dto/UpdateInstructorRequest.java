package org.redcode.bookanddriveservice.instructors.controller.dto;

import lombok.Builder;

@Builder
public record UpdateInstructorRequest(
    String name,
    String sureName,
    String email
) {
}
