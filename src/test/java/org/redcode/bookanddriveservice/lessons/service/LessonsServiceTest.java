package org.redcode.bookanddriveservice.lessons.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.redcode.bookanddriveservice.lessons.utils.LessonsDataGenerator.generateInvalidLesson;
import static org.redcode.bookanddriveservice.lessons.utils.LessonsDataGenerator.generateLesson;
import static org.redcode.bookanddriveservice.lessons.utils.LessonsDataGenerator.generateLessonEntity;
import static org.redcode.bookanddriveservice.lessons.utils.LessonsDataGenerator.generateLessonSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
import org.redcode.bookanddriveservice.exceptions.ValidationException;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.redcode.bookanddriveservice.lessons.repository.LessonCustomSearchRepository;
import org.redcode.bookanddriveservice.lessons.repository.LessonSearchCriteria;
import org.redcode.bookanddriveservice.lessons.repository.LessonsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class LessonsServiceTest {

    @Mock
    private LessonsRepository lessonsRepository;

    @Mock
    private LessonCustomSearchRepository lessonCustomRepository;

    @InjectMocks
    private LessonsService lessonsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_shouldCreateLessonWhenDatesAreValid() {
        // Arrange
        Lesson lesson = generateLesson();
        LessonEntity lessonEntity = LessonEntity.from(lesson);

        when(lessonsRepository.save(any(LessonEntity.class))).thenReturn(lessonEntity);

        // Act
        Lesson result = lessonsService.create(lesson);

        // Assert
        assertNotNull(result);
        assertEquals(lesson.getStartTime(), result.getStartTime());
        assertEquals(lesson.getEndTime(), result.getEndTime());
    }

    @Test
    void testCreate_shouldThrowValidationExceptionWhenStartTimeIsAfterEndTime() {
        // Arrange
        Lesson lesson = generateInvalidLesson();

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> lessonsService.create(lesson)
        );
        assertEquals("invalid_dates", exception.getReason());
        assertEquals("Start time cannot be greater than end time", exception.getMessage());
    }

    @Test
    void testFindByCriteria_shouldReturnLessonsPage() {
        // Arrange
        LessonSearchCriteria criteria = generateLessonSearchCriteria();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<LessonEntity> lessonEntities = List.of(
            generateLessonEntity()
        );
        List<Lesson> expectedLessons = lessonEntities.stream().map(Lesson::from).toList();

        when(lessonCustomRepository.findAllByCriteria(criteria, pageRequest)).thenReturn(lessonEntities);
        when(lessonCustomRepository.getTotalCount(criteria)).thenReturn((long) lessonEntities.size());

        // Act
        Page<Lesson> result = lessonsService.findByCriteria(criteria, pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedLessons.size(), result.getContent().size());
        assertEquals(expectedLessons, result.getContent());
    }

    @Test
    void testDeleteById_shouldDeleteLessonWhenIdExists() {
        // Arrange
        UUID lessonId = UUID.randomUUID();
        LessonEntity lessonEntity = new LessonEntity();
        lessonEntity.setId(lessonId);

        when(lessonsRepository.findById(lessonId)).thenReturn(Optional.of(lessonEntity));

        // Act
        assertDoesNotThrow(() -> lessonsService.deleteById(lessonId));

        // Assert
        verify(lessonsRepository).deleteById(lessonId);
    }

    @Test
    void testDeleteById_shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        UUID lessonId = UUID.randomUUID();

        when(lessonsRepository.findById(lessonId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> lessonsService.deleteById(lessonId)
        );
        assertEquals("Resource not found.", exception.getMessage());
    }
}
