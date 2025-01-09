package org.redcode.bookanddriveservice.lessons.repository;

import java.util.UUID;
import org.redcode.bookanddriveservice.lessons.model.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, UUID> {

}
