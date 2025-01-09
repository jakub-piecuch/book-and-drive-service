package org.redcode.bookanddriveservice.trainees.repository;

import java.util.UUID;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineesRepository extends JpaRepository<TraineeEntity, UUID> {

}
