package org.redcode.bookanddriveservice.lessons.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.redcode.bookanddriveservice.cars.dto.CarResponse;
import org.redcode.bookanddriveservice.instructors.dto.InstructorResponse;
import org.redcode.bookanddriveservice.trainees.dto.TraineeResponse;

@Builder
public class LessonResponse {
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private InstructorResponse instructor;
    private TraineeResponse trainee;
    private CarResponse car;

    public static LessonResponse from(Lesson lesson) {
        return LessonResponse.builder()
            .id(lesson.getId())
            .startTime(lesson.getStartTime())
            .endTime(lesson.getEndTime())
            .instructor(InstructorResponse.from(lesson.getInstructor()))
            .trainee(TraineeResponse.from(lesson.getTrainee()))
            .car(CarResponse.from(lesson.getCar()))
            .build();
    }
}
