package org.redcode.bookanddriveservice.trainees.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.trainees.dto.Trainee;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;
import org.redcode.bookanddriveservice.trainees.repository.TraineesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraineeService {
    
    private final TraineesRepository repository;

    public Trainee create(Trainee trainee) {
        TraineeEntity traineeEntity = TraineeEntity.from(trainee);
        TraineeEntity savedTrainee = repository.save(traineeEntity);

        return Trainee.from(savedTrainee);
    }

    public List<Trainee> getCars() {
        return repository.findAll().stream()
            .map(Trainee::from)
            .toList();
    }

    public Trainee findById(UUID id) {
        return repository.findById(id)
            .map(Trainee::from)
            .orElse(null);
    }

    public Trainee updateById(UUID id, Trainee trainee) {
        return repository.findById(id)
            .map(traineeEntity -> {
                TraineeEntity updatedTraineeEntity = TraineeEntity.from(trainee);
                updatedTraineeEntity.setId(id);
                TraineeEntity savedTrainee = repository.save(updatedTraineeEntity);
                return Trainee.from(savedTrainee);
            })
            .orElse(null);
    }

    public UUID deleteById(UUID id) {
        return repository.findById(id)
            .map(traineeEntity -> {
                repository.deleteById(id);
                return id;
            })
            .orElse(null);
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
