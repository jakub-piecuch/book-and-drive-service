package org.redcode.bookanddriveservice.lessons.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.lessons.service.LessonsService;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
class LessonsControllerTest {

    @Mock
    LessonsService lessonsService; // Mock of the service

    private MockMvc mockMvc;

    @InjectMocks
    private LessonsController lessonsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonsController).build();
    }

    @Test
    void testCreateLesson() throws Exception {
        var instructorId = UUID.randomUUID();
        var traineeId = UUID.randomUUID();
        Lesson lesson = Lesson.builder()
            .id(UUID.randomUUID())
            .startTime(LocalDateTime.of(LocalDate.of(2025, 1, 12), LocalTime.of(12, 25)))
            .endTime(LocalDateTime.of(LocalDate.of(2025, 1, 12), LocalTime.of(13, 25)))
            .instructor(Instructor.builder().id(instructorId).build())
            .trainee(Trainee.builder().id(traineeId).build())
            .build();

        String content = String.format(
            "{\"startTime\":\"2025-01-12T12:25\",\"endTime\":\"2025-01-12T13:25\",\"instructorId\":\"%s\",\"traineeId\":\"%s\"}",
            instructorId,
            traineeId
        );

        when(lessonsService.create(any(Lesson.class))).thenReturn(lesson);

        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.startTime", Matchers.contains(2025, 1, 12, 12, 25)))
            .andExpect(jsonPath("$.endTime", Matchers.contains(2025, 1, 12, 13, 25)))
            .andExpect(jsonPath("$.instructorId").value(instructorId.toString()))
            .andExpect(jsonPath("$.traineeId").value(traineeId.toString()));
    }

    @Test
    void fetchAllByCriteria() {
    }

    @Test
    void deleteLessonById() {
    }
}
