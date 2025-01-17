package org.redcode.bookanddriveservice.instructors.service;

import static org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException.RESOURECE_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.exceptions.ResourceNotFoundException;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;
import org.redcode.bookanddriveservice.instructors.repository.InstructorsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorsService {
    
    private final InstructorsRepository repository;

    public Instructor create(Instructor instructor) {
        InstructorEntity instructorEntity = InstructorEntity.from(instructor);
        InstructorEntity savedInstructor = repository.save(instructorEntity);

        return Instructor.from(savedInstructor);
    }

    public List<Instructor> getInstructors() {
        return repository.findAll().stream()
            .map(Instructor::from)
            .toList();
    }

    public Instructor findById(UUID id) {
        return repository.findById(id)
            .map(Instructor::from)
            .orElse(null);
    }

    public Instructor updateById(UUID id, Instructor instructor) {
        return repository.findById(id)
            .map(instructorEntity -> {
                InstructorEntity updatedInstructorEntity = InstructorEntity.update(instructorEntity, instructor);
                InstructorEntity savedInstructor = repository.save(updatedInstructorEntity);
                return Instructor.from(savedInstructor);
            })
            .orElseThrow(() -> ResourceNotFoundException.of(RESOURECE_NOT_FOUND));
    }

    public void deleteById(UUID id) {
        repository.findById(id)
            .ifPresentOrElse(
                instructorEntity -> repository.deleteById(id), () -> {
                    throw ResourceNotFoundException.of(RESOURECE_NOT_FOUND);
                }
            );
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
