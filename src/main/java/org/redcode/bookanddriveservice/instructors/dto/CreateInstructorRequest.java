package org.redcode.bookanddriveservice.instructors.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record CreateInstructorRequest(
    String name,
    String sureName
) {

}
