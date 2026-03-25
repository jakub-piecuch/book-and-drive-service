package org.redcode.bookanddriveservice.trainees.utils;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;

@UtilityClass
public class TraineesDataGenerator {

    private static final UUID TRAINEE_ID = UUID.randomUUID();
    private static final String NAME = "John";
    private static final String SURNAME = "Doe";
    private static final String EMAIL = "johndoe@gmail.com";
    private static final String PHONE_NUMBER = "+48123456789";

    public static Trainee generateTrainee() {
        return Trainee.builder()
            .id(TRAINEE_ID)
            .name(NAME)
            .surname(SURNAME)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .build();
    }

    public static TraineeEntity generateTraineeEntity() {
        return TraineeEntity.builder()
            .id(TRAINEE_ID)
            .name(NAME)
            .surname(SURNAME)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .build();
    }
}
