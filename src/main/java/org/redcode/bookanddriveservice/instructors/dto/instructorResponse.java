package org.redcode.bookanddriveservice.instructors.dto;

import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record instructorResponse(
    UUID id,
    String name,
    String sureName
) {

}
