package org.redcode.bookanddriveservice.instructors.repository;

import java.util.UUID;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorsRepository extends JpaRepository<InstructorEntity, UUID> {

}
