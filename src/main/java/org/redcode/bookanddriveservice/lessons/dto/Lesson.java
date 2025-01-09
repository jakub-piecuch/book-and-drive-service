package org.redcode.bookanddriveservice.lessons.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.instructors.dto.Instructor;
import org.redcode.bookanddriveservice.trainees.dto.Trainee;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instructor instructor;
    private Trainee trainee;
    private Car car;
}
