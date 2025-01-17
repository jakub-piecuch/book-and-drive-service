package org.redcode.bookanddriveservice.lessons.utils;

import static org.redcode.bookanddriveservice.instructors.utils.InstructorsDataGenerator.generateInstructor;
import static org.redcode.bookanddriveservice.instructors.utils.InstructorsDataGenerator.generateInstructorEntity;
import static org.redcode.bookanddriveservice.trainees.utils.TraineesDataGenerator.generateTrainee;
import static org.redcode.bookanddriveservice.trainees.utils.TraineesDataGenerator.generateTraineeEntity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.redcode.bookanddriveservice.lessons.repository.LessonSearchCriteria;

@UtilityClass
public class LessonsDataGenerator {

    private static final UUID LESSON_ID = UUID.randomUUID();
    private static final LocalDateTime START_TIME = LocalDateTime.of(2023, 10, 1, 10, 0);
    private static final LocalDateTime END_TIME = LocalDateTime.of(2023, 10, 1, 12, 0);
    private static final String TITLE = "Mathematics Lesson";
    private static final UUID INSTRUCTOR_ID = UUID.randomUUID();
    private static final UUID TRAINEE_ID = UUID.randomUUID();

    public static Lesson generateLesson() {
        return Lesson.builder()
            .id(LESSON_ID)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .instructor(generateInstructor())
            .trainee(generateTrainee())
            .build();
    }

    public static Lesson generateInvalidLesson() {
        return Lesson.builder()
            .id(LESSON_ID)
            .startTime(END_TIME)
            .endTime(START_TIME)
            .instructor(generateInstructor())
            .trainee(generateTrainee())
            .build();
    }

    public static LessonEntity generateLessonEntity() {
        return LessonEntity.builder()
            .id(LESSON_ID)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .instructor(generateInstructorEntity())
            .trainee(generateTraineeEntity())
            .build();
    }

    public static LessonSearchCriteria generateLessonSearchCriteria() {
        return LessonSearchCriteria.builder()
            .startTime(LocalDateTime.of(2025, 1, 12, 12, 25))
            .endTime(LocalDateTime.of(2025, 1, 12, 12, 35))
            .instructorId(INSTRUCTOR_ID)
            .traineeId(TRAINEE_ID)
            .build();
    }
}
