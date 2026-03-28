package org.redcode.bookanddriveservice.lessons.domain;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.cars.domain.Car;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.lessons.controller.dto.CreateLessonRequest;
import org.redcode.bookanddriveservice.lessons.controller.dto.UpdateLessonRequest;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private UUID id;
    private String tenantId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instructor instructor;
    private Trainee trainee;
    private Car car;

    public static Lesson from(LessonEntity lesson) {
        return Lesson.builder()
            .id(lesson.getId())
            .tenantId(lesson.getTenantId())
            .startTime(lesson.getStartTime())
            .endTime(lesson.getEndTime())
            .instructor(Instructor.from(lesson.getInstructor()))
            .trainee(Trainee.from(lesson.getTrainee()))
            .car(Car.from(lesson.getCar()))
            .build();
    }

    public static Lesson from(UpdateLessonRequest request) {
        LessonBuilder lessonBuilder = Lesson.builder()
            .startTime(request.startTime())
            .endTime(request.endTime())
            .instructor(Instructor.builder().id(request.instructorId()).build())
            .trainee(Trainee.builder().id(request.traineeId()).build());

        if (nonNull(request.carId())) {
            lessonBuilder.car(Car.builder().id(request.carId()).build());
        }

        return lessonBuilder.build();
    }

    public static Lesson from(CreateLessonRequest lesson) {
        LessonBuilder lessonBuilder = Lesson.builder()
            .startTime(lesson.startTime())
            .endTime(lesson.endTime())
            .instructor(Instructor.builder()
                .id(lesson.instructorId())
                .build())
            .trainee(Trainee.builder()
                .id(lesson.traineeId())
                .build());

        if (nonNull(lesson.carId())) {
            lessonBuilder.car(Car.builder().id(lesson.carId()).build());
        }

        return lessonBuilder.build();
    }
}
