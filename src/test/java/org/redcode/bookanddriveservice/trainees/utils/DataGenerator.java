package org.redcode.bookanddriveservice.trainees.utils;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;

@UtilityClass
public class DataGenerator {

    private static final UUID TRAINEE_ID = UUID.randomUUID();
    private static final String NAME = "John";
    private static final String SURE_NAME = "Doe";
    private static final String EMAIL = "johndoe@gmail.com";

    public static Trainee generateTrainee() {
        return Trainee.builder()
            .id(TRAINEE_ID)
            .name(NAME)
            .sureName(SURE_NAME)
            .email(EMAIL)
            .build();
    }

    public static TraineeEntity generateTraineeEntity() {
        return TraineeEntity.builder()
            .id(TRAINEE_ID)
            .name(NAME)
            .sureName(SURE_NAME)
            .email(EMAIL)
            .build();
    }
}
