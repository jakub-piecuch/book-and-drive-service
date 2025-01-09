package org.redcode.bookanddriveservice.instructors.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Instructor {
    private UUID id;
    private String name;
    private String sureName;
}
