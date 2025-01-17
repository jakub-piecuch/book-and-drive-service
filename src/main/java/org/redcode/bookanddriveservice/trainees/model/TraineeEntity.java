package org.redcode.bookanddriveservice.trainees.model;

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
import org.redcode.bookanddriveservice.trainees.domain.Trainee;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainee")
public class TraineeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String sureName;
    @Column(unique = true, nullable = false)
    private String email;

    public static TraineeEntity from(Trainee trainee){
        return TraineeEntity.builder()
            .id(trainee.getId())
            .name(trainee.getName())
            .sureName(trainee.getSureName())
            .email(trainee.getEmail())
            .build();
    }

    public static TraineeEntity update(TraineeEntity traineeEntity, Trainee trainee) {
        return TraineeEntity.builder()
            .id(traineeEntity.getId())
            .name(trainee.getName())
            .sureName(trainee.getSureName())
            .email(trainee.getEmail())
            .build();
    }
}
