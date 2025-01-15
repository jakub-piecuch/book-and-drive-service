package org.redcode.bookanddriveservice.trainees.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.trainees.controller.dto.CreateTraineeRequest;
import org.redcode.bookanddriveservice.trainees.controller.dto.UpdateTraineeRequest;
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

    public static Trainee from(CreateTraineeRequest request) {
        return Trainee.builder()
            .name(request.name())
            .sureName(request.sureName())
            .email(request.email())
            .build();
    }


    public static Trainee from(UpdateTraineeRequest request) {
        return Trainee.builder()
            .name(request.name())
            .sureName(request.sureName())
            .email(request.email())
            .build();
    }
}
