package org.redcode.bookanddriveservice.instructors.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Instructor {
    private UUID id;
    private String name;
    private String sureName;
    private String email;

    public static Instructor from(InstructorEntity instructor) {
        return Instructor.builder()
            .id(instructor.getId())
            .name(instructor.getName())
            .sureName(instructor.getSureName())
            .email(instructor.getEmail())
            .build();
    }

    public static Instructor from(CreateInstructorRequest instructor) {
        return Instructor.builder()
            .name(instructor.name())
            .sureName(instructor.sureName())
            .email(instructor.email())
            .build();
    }

    public static Instructor from(UpdateInstructorRequest instructor) {
        return Instructor.builder()
            .name(instructor.name())
            .sureName(instructor.sureName())
            .email(instructor.email())
            .build();
    }
}
