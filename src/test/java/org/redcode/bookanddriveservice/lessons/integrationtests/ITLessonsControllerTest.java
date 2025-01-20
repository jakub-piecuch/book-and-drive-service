package org.redcode.bookanddriveservice.lessons.integrationtests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.redcode.bookanddriveservice.instructors.utils.InstructorsDataGenerator.generateInstructorEntity;
import static org.redcode.bookanddriveservice.lessons.utils.LessonsDataGenerator.generateLessonEntity;
import static org.redcode.bookanddriveservice.trainees.utils.TraineesDataGenerator.generateTraineeEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;
import org.redcode.bookanddriveservice.instructors.repository.InstructorsRepository;
import org.redcode.bookanddriveservice.lessons.controller.dto.CreateLessonRequest;
import org.redcode.bookanddriveservice.lessons.controller.dto.LessonResponse;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.redcode.bookanddriveservice.lessons.repository.LessonsRepository;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;
import org.redcode.bookanddriveservice.trainees.repository.TraineesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITLessonsControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    LessonsRepository lessonsRepository;
    @Autowired
    InstructorsRepository instructorsRepository;
    @Autowired
    TraineesRepository traineesRepository;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "lesson", "instructor", "trainee");
    }

    @Test
    void shouldCreateLesson() {
        InstructorEntity instructorEntity = createInstructorEntity();
        TraineeEntity trainee = createTraineeEntity();
        CreateLessonRequest request = CreateLessonRequest.builder()
            .startTime(LocalDateTime.of(2025,12,12,12,12))
            .endTime(LocalDateTime.of(2025,12,12,13,13))
            .instructorId(instructorEntity.getId())
            .traineeId(trainee.getId())
            .build();

        var response = restTemplate.postForEntity("/api/lessons", request, LessonResponse.class).getBody();

        assertThat(response.id()).isNotNull();
        assertThat(response.startTime()).isEqualTo(LocalDateTime.of(2025,12,12,12,12));
        assertThat(response.endTime()).isEqualTo(LocalDateTime.of(2025,12,12,13,13));
        assertThat(response.instructorId()).isEqualTo(instructorEntity.getId());
        assertThat(response.traineeId()).isEqualTo(trainee.getId());
        assertThat(response.carId()).isNull();
    }

//    @Test
//    void shouldNotCreateLesson_duplicate() {
//        var lesson = insertLessonInDb();
//        CreateLessonRequest request = CreateLessonRequest.builder()
//            .startTime(LocalDateTime.of(2025,12,12,12,12))
//            .endTime(LocalDateTime.of(2025,12,12,13,13))
//            .instructorId(lesson.getInstructor().getId())
//            .traineeId(lesson.getTrainee().getId())
//            .build();
//
//        assertThrows(Exception.class, () -> {
//            restTemplate.postForEntity("/api/lessons", request, LessonResponse.class);
//        });
//    }

    private LessonEntity insertLessonInDb() {
        var instructor = createInstructorEntity();
        var trainee = createTraineeEntity();

        var entity = generateLessonEntity();
        entity.setId(null);
        entity.setInstructor(instructor);
        entity.setTrainee(trainee);
        entity.setCar(null);

        return lessonsRepository.save(entity);
    }

    @Transactional
    private @NotNull TraineeEntity createTraineeEntity() {
        var trainee = generateTraineeEntity();
        trainee.setId(null);
        var train = traineesRepository.saveAndFlush(trainee);
        trainee.setId(train.getId());
        return trainee;
    }

    @Transactional
    private @NotNull InstructorEntity createInstructorEntity() {
        var instructor = generateInstructorEntity();
        instructor.setId(null);
        var instr = instructorsRepository.saveAndFlush(instructor);
        instructor.setId(instr.getId());
        return instructor;
    }
}
