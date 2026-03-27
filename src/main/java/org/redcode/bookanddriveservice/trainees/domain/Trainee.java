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
    private String tenantId;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    public static Trainee from(TraineeEntity trainee) {
        return Trainee.builder()
            .id(trainee.getId())
            .tenantId(trainee.getTenantId())
            .name(trainee.getName())
            .surname(trainee.getSurname())
            .email(trainee.getEmail())
            .phoneNumber(trainee.getPhoneNumber())
            .build();
    }

    public static Trainee from(CreateTraineeRequest request) {
        return Trainee.builder()
            .name(request.name())
            .surname(request.surname())
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .build();
    }


    public static Trainee from(UpdateTraineeRequest request) {
        return Trainee.builder()
            .name(request.name())
            .surname(request.surname())
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .build();
    }
}
