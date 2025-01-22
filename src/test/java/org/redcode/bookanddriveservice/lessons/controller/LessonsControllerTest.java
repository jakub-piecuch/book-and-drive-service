package org.redcode.bookanddriveservice.lessons.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.lessons.service.LessonsService;
import org.redcode.bookanddriveservice.lessons.utils.LessonsDataGenerator;
import org.redcode.bookanddriveservice.page.PageResponse;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.springframework.data.domain.PageRequest;
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
        Lesson lesson = Lesson.builder().id(UUID.randomUUID()).startTime(LocalDateTime.of(LocalDate.of(2025, 1, 12), LocalTime.of(12, 25))).endTime(LocalDateTime.of(LocalDate.of(2025, 1, 12), LocalTime.of(13, 25))).instructor(Instructor.builder().id(instructorId).build()).trainee(Trainee.builder().id(traineeId).build()).build();

        String content = String.format("{\"startTime\":\"2025-01-12T12:25\",\"endTime\":\"2025-01-12T13:25\",\"instructorId\":\"%s\",\"traineeId\":\"%s\"}", instructorId, traineeId);

        when(lessonsService.create(any(Lesson.class))).thenReturn(lesson);

        mockMvc.perform(post("/api/lessons").contentType(MediaType.APPLICATION_JSON).content(content))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.startTime").value(lesson.getStartTime().toString()))
            .andExpect(jsonPath("$.endTime").value(lesson.getEndTime().toString()))
            .andExpect(jsonPath("$.instructorId").value(instructorId.toString()))
            .andExpect(jsonPath("$.traineeId").value(traineeId.toString()));
    }

    @Test
    void testFetchAllByCriteria_shouldReturnLessonsPageSuccessfully() throws Exception {
        // Arrange
        UUID instructorId = UUID.randomUUID();
        UUID traineeId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        LocalDateTime startDateTime = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 12, 0);
        int page = 0;
        int limit = 5;

        Lesson lesson = LessonsDataGenerator.generateLesson();
        List<Lesson> lessons = List.of(lesson);

        // Using the new PageResponse class
        PageResponse<Lesson> lessonPageResponse = PageResponse.of(
            lessons,
            new PageResponse.PageMetadata(page, limit, lessons.size(), lessons.size())
        );

        when(lessonsService.findByCriteria(any(), eq(PageRequest.of(page, limit)))).thenReturn(lessonPageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/lessons")
                .param("startDateTime", startDateTime.toString())
                .param("endDateTime", endDateTime.toString())
                .param("instructorId", instructorId.toString())
                .param("traineeId", traineeId.toString())
                .param("carId", carId.toString())
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].id").value(lesson.getId().toString()))
            .andExpect(jsonPath("$.content[0].startTime").value(lesson.getStartTime().toString()))
            .andExpect(jsonPath("$.content[0].endTime").value(lesson.getEndTime().toString()))
            .andExpect(jsonPath("$.page.page").value(page))
            .andExpect(jsonPath("$.page.limit").value(limit))
            .andExpect(jsonPath("$.page.totalElements").value(lessons.size()))
            .andExpect(jsonPath("$.page.totalPages").value(1));
    }

    @Test
    void testFetchAllByCriteria_shouldReturnEmptyPageWhenNoLessonsFound() throws Exception {
        // Arrange
        LocalDateTime startDateTime = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 12, 0);
        PageResponse<Lesson> emptyPage = PageResponse.of(List.of(), new PageResponse.PageMetadata(0,0,0,0));

        when(lessonsService.findByCriteria(any(), any())).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/api/lessons")
                .param("startDateTime", startDateTime.toString())
                .param("endDateTime", endDateTime.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    void deleteLessonById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/lessons/{id}", id)).andExpect(status().isNoContent());
    }
}
