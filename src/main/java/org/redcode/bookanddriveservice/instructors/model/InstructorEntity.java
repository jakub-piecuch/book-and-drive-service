package org.redcode.bookanddriveservice.instructors.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "instructor")
public class InstructorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String sureName;
    @Column(unique = true, nullable = false)
    private String email;

    public static InstructorEntity from(Instructor instructor) {
        return InstructorEntity.builder()
            .id(instructor.getId())
            .name(instructor.getName())
            .sureName(instructor.getSureName())
            .email(instructor.getEmail())
            .build();
    }
}
