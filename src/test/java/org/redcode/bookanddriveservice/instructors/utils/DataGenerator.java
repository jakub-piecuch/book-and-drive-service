package org.redcode.bookanddriveservice.instructors.utils;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;

@UtilityClass
public class DataGenerator {

    private static final UUID INSTRUCTOR_ID = UUID.randomUUID();
    private static final String NAME = "Jan";
    private static final String SURE_NAME = "Kowalski";
    private static final String EMAIL = "kowalski@gmail.com";

    public static Instructor generateInstructor() {
        return Instructor.builder()
            .id(INSTRUCTOR_ID)
            .name(NAME)
            .sureName(SURE_NAME)
            .email(EMAIL)
            .build();
    }

    public static InstructorEntity generateInstructorEntity() {
        return InstructorEntity.builder()
            .id(INSTRUCTOR_ID)
            .name(NAME)
            .sureName(SURE_NAME)
            .email(EMAIL)
            .build();
    }
}
