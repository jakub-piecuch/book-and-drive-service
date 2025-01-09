package org.redcode.bookanddriveservice.instructors.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateInstructorRequest(
    String name,
    String sureName
) {
}
