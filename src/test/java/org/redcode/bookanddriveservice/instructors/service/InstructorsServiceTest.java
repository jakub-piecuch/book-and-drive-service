package org.redcode.bookanddriveservice.instructors.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;
import org.redcode.bookanddriveservice.instructors.repository.InstructorsRepository;
import org.redcode.bookanddriveservice.instructors.utils.DataGenerator;

class InstructorsServiceTest {

    @Mock
    private InstructorsRepository instructorsRepository;

    @InjectMocks
    private InstructorsService instructorsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Instructor instructor = DataGenerator.generateInstructor();
        InstructorEntity savedInstructorEntity = DataGenerator.generateInstructorEntity();

        when(instructorsRepository.save(any(InstructorEntity.class))).thenReturn(savedInstructorEntity);

        Instructor result = instructorsService.create(instructor);

        assertNotNull(result);
        assertEquals(instructor, result);
    }

    @Test
    void testGetInstructors() {
        InstructorEntity instructorEntity = DataGenerator.generateInstructorEntity();
        Instructor instructor = DataGenerator.generateInstructor();

        when(instructorsRepository.findAll()).thenReturn(List.of(instructorEntity));

        List<Instructor> result = instructorsService.getInstructors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(instructor, result.get(0));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Instructor instructor = DataGenerator.generateInstructor();
        InstructorEntity instructorEntity = DataGenerator.generateInstructorEntity();

        when(instructorsRepository.findById(id)).thenReturn(Optional.of(instructorEntity));

        Instructor result = instructorsService.findById(id);

        assertNotNull(result);
        assertEquals(instructor, result);
    }

    @Test
    void testUpdateById() {
        UUID id = UUID.randomUUID();
        Instructor instructor = DataGenerator.generateInstructor();
        Instructor updatedInstructor = instructor;
        InstructorEntity instructorEntity = DataGenerator.generateInstructorEntity();
        InstructorEntity updatedInstructorEntity = instructorEntity;
        updatedInstructor.setName("Updated Name");
        updatedInstructorEntity.setName("Updated Name");

        when(instructorsRepository.findById(id)).thenReturn(Optional.of(instructorEntity));
        when(instructorsRepository.save(any(InstructorEntity.class))).thenReturn(updatedInstructorEntity);

        Instructor result = instructorsService.updateById(id, instructor);

        assertNotNull(result);
        assertEquals(updatedInstructor, result);
    }

    @Test
    void deleteById_shouldDeleteInstructorWhenIdExists() {
        // Arrange
        UUID instructorId = UUID.randomUUID();
        InstructorEntity instructorEntity = new InstructorEntity();
        instructorEntity.setId(instructorId);

        when(instructorsRepository.findById(instructorId)).thenReturn(Optional.of(instructorEntity));

        // Act
        assertDoesNotThrow(() -> instructorsService.deleteById(instructorId));

        // Assert
        verify(instructorsRepository, times(1)).deleteById(instructorId);
    }

    @Test
    void deleteById_shouldThrowExceptionWhenIdDoesNotExist() {
        // Arrange
        UUID instructorId = UUID.randomUUID();
        when(instructorsRepository.findById(instructorId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> instructorsService.deleteById(instructorId)
        );

        assertEquals("Resource not found.", exception.getMessage());
        verify(instructorsRepository, never()).deleteById(instructorId);
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();

        when(instructorsRepository.existsById(id)).thenReturn(true);

        boolean result = instructorsService.existsById(id);

        assertTrue(result);
    }
}
