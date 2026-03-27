package org.redcode.bookanddriveservice.trainees.model;

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
import org.redcode.bookanddriveservice.trainees.domain.Trainee;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainee", uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "email"}))
public class TraineeEntity {
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

    public static TraineeEntity from(Trainee trainee){
        return TraineeEntity.builder()
            .id(trainee.getId())
            .name(trainee.getName())
            .surname(trainee.getSurname())
            .email(trainee.getEmail())
            .phoneNumber(trainee.getPhoneNumber())
            .build();
    }

    public static TraineeEntity update(TraineeEntity traineeEntity, Trainee trainee) {
        return TraineeEntity.builder()
            .id(traineeEntity.getId())
            .name(trainee.getName())
            .surname(trainee.getSurname())
            .email(trainee.getEmail())
            .phoneNumber(trainee.getPhoneNumber())
            .build();
    }
}
