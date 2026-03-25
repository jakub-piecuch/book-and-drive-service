package org.redcode.bookanddriveservice.instructors.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TenantId;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "instructor", uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "email"}))
public class InstructorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @TenantId
    private String tenantId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;

    public static InstructorEntity from(Instructor instructor) {
        return InstructorEntity.builder()
            .id(instructor.getId())
            .name(instructor.getName())
            .surname(instructor.getSurname())
            .email(instructor.getEmail())
            .phoneNumber(instructor.getPhoneNumber())
            .build();
    }

    public static InstructorEntity update(InstructorEntity instructorEntity, Instructor instructor) {
        return InstructorEntity.builder()
            .id(instructorEntity.getId())
            .name(instructor.getName())
            .surname(instructor.getSurname())
            .email(instructor.getEmail())
            .phoneNumber(instructor.getPhoneNumber())
            .build();
    }
}
