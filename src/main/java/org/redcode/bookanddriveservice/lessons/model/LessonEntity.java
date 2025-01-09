package org.redcode.bookanddriveservice.lessons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;
import org.redcode.bookanddriveservice.lessons.dto.Lesson;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "instructors")
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorEntity instructor;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    private TraineeEntity trainee;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarEntity car;

    public static LessonEntity from(Lesson lesson){
        return LessonEntity.builder()
            .id(lesson.getId())
            .startTime(lesson.getStartTime())
            .endTime(lesson.getEndTime())
            .instructor(InstructorEntity.from(lesson.getInstructor()))
            .trainee(TraineeEntity.from(lesson.getTrainee()))
            .car(CarEntity.from(lesson.getCar()))
            .build();
    }
}
