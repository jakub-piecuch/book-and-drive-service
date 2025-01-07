package org.redcode.bookanddriveservice.cars.repository;

import java.util.UUID;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarsRepository extends JpaRepository<CarEntity, UUID> {

}
