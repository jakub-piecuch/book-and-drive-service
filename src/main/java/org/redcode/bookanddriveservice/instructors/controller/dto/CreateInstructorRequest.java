package org.redcode.bookanddriveservice.instructors.controller.dto;

import lombok.Builder;

@Builder
public record CreateInstructorRequest(
    String name,
    String sureName,
    String email
) {

}
