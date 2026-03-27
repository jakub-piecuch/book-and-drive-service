package org.redcode.bookanddriveservice.instructors.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.redcode.bookanddriveservice.instructors.controller.dto.CreateInstructorRequest;
import org.redcode.bookanddriveservice.instructors.controller.dto.UpdateInstructorRequest;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Instructor {
    private UUID id;
    private String tenantId;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    public static Instructor from(InstructorEntity instructor) {
        return Instructor.builder()
            .id(instructor.getId())
            .tenantId(instructor.getTenantId())
            .name(instructor.getName())
            .surname(instructor.getSurname())
            .email(instructor.getEmail())
            .phoneNumber(instructor.getPhoneNumber())
            .build();
    }

    public static Instructor from(CreateInstructorRequest instructor) {
        return Instructor.builder()
            .name(instructor.name())
            .surname(instructor.surname())
            .email(instructor.email())
            .phoneNumber(instructor.phoneNumber())
            .build();
    }

    public static Instructor from(UpdateInstructorRequest instructor) {
        return Instructor.builder()
            .name(instructor.name())
            .surname(instructor.surname())
            .email(instructor.email())
            .phoneNumber(instructor.phoneNumber())
            .build();
    }
}
