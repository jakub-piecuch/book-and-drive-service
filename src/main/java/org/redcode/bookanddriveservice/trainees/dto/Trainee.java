package org.redcode.bookanddriveservice.trainees.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    private UUID id;
    private String name;
    private String sureName;
    private String email;

    public static Trainee from(TraineeEntity trainee) {
        return Trainee.builder()
            .id(trainee.getId())
            .name(trainee.getName())
            .sureName(trainee.getSureName())
            .email(trainee.getEmail())
            .build();
    }
}
