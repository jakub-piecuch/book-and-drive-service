package org.redcode.bookanddriveservice.trainees.service;

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
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;
import org.redcode.bookanddriveservice.trainees.repository.TraineesRepository;
import org.redcode.bookanddriveservice.trainees.utils.TraineesDataGenerator;

class TraineesServiceTest {

    @Mock
    private TraineesRepository traineesRepository;

    @InjectMocks
    private TraineesService traineesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Trainee trainee = TraineesDataGenerator.generateTrainee();
        TraineeEntity savedTraineeEntity = TraineesDataGenerator.generateTraineeEntity();

        when(traineesRepository.save(any(TraineeEntity.class))).thenReturn(savedTraineeEntity);

        Trainee result = traineesService.create(trainee);

        assertNotNull(result);
        assertEquals(trainee, result);
    }

    @Test
    void testGetTrainees() {
        TraineeEntity traineeEntity = TraineesDataGenerator.generateTraineeEntity();
        Trainee trainee = TraineesDataGenerator.generateTrainee();

        when(traineesRepository.findAll()).thenReturn(List.of(traineeEntity));

        List<Trainee> result = traineesService.getTrainees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainee, result.get(0));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Trainee trainee = TraineesDataGenerator.generateTrainee();
        TraineeEntity traineeEntity = TraineesDataGenerator.generateTraineeEntity();

        when(traineesRepository.findById(id)).thenReturn(Optional.of(traineeEntity));

        Trainee result = traineesService.findById(id);

        assertNotNull(result);
        assertEquals(trainee, result);
    }

    @Test
    void testUpdateById() {
        UUID id = UUID.randomUUID();
        Trainee trainee = TraineesDataGenerator.generateTrainee();
        Trainee updatedTrainee = trainee;
        TraineeEntity traineeEntity = TraineesDataGenerator.generateTraineeEntity();
        TraineeEntity updatedTraineeEntity = traineeEntity;
        updatedTrainee.setName("Updated Name");
        updatedTraineeEntity.setName("Updated Name");

        when(traineesRepository.findById(id)).thenReturn(Optional.of(traineeEntity));
        when(traineesRepository.save(any(TraineeEntity.class))).thenReturn(updatedTraineeEntity);

        Trainee result = traineesService.updateById(id, trainee);

        assertNotNull(result);
        assertEquals(updatedTrainee, result);
    }

    @Test
    void deleteById_shouldDeleteTraineeWhenIdExists() {
        // Arrange
        UUID traineeId = UUID.randomUUID();
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setId(traineeId);

        when(traineesRepository.findById(traineeId)).thenReturn(Optional.of(traineeEntity));

        // Act
        assertDoesNotThrow(() -> traineesService.deleteById(traineeId));

        // Assert
        verify(traineesRepository, times(1)).deleteById(traineeId);
    }

    @Test
    void deleteById_shouldThrowExceptionWhenIdDoesNotExist() {
        // Arrange
        UUID traineeId = UUID.randomUUID();
        when(traineesRepository.findById(traineeId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> traineesService.deleteById(traineeId)
        );

        assertEquals("Resource not found.", exception.getMessage());
        verify(traineesRepository, never()).deleteById(traineeId);
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();

        when(traineesRepository.existsById(id)).thenReturn(true);

        boolean result = traineesService.existsById(id);

        assertTrue(result);
    }
}
