package org.redcode.bookanddriveservice.trainees.service;

import static org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException.RESOURECE_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;
import org.redcode.bookanddriveservice.trainees.repository.TraineesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraineesService {
    
    private final TraineesRepository repository;

    public Trainee create(Trainee trainee) {
        TraineeEntity traineeEntity = TraineeEntity.from(trainee);
        TraineeEntity savedTrainee = repository.save(traineeEntity);

        return Trainee.from(savedTrainee);
    }

    public List<Trainee> getTrainees() {
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
                TraineeEntity updatedTraineeEntity = TraineeEntity.update(traineeEntity, trainee);
                TraineeEntity savedTrainee = repository.save(updatedTraineeEntity);
                return Trainee.from(savedTrainee);
            })
            .orElseThrow(() -> ResourceNotFoundException.of(RESOURECE_NOT_FOUND));
    }

    public void deleteById(UUID id) {
        repository.findById(id)
            .ifPresentOrElse(
                traineeEntity -> repository.deleteById(id), () -> {
                    throw ResourceNotFoundException.of(RESOURECE_NOT_FOUND);
                }
            );
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
