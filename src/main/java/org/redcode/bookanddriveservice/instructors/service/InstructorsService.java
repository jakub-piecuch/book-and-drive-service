package org.redcode.bookanddriveservice.instructors.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
                InstructorEntity updatedInstructorEntity = InstructorEntity.from(instructor);
                updatedInstructorEntity.setId(id);
                InstructorEntity savedInstructor = repository.save(updatedInstructorEntity);
                return Instructor.from(savedInstructor);
            })
            .orElse(null);
    }

    public UUID deleteById(UUID id) {
        return repository.findById(id)
            .map(instructorEntity -> {
                repository.deleteById(id);
                return id;
            })
            .orElse(null);
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
