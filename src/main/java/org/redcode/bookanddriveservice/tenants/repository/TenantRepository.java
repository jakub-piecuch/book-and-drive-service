package org.redcode.bookanddriveservice.tenants.repository;

import java.util.UUID;
import org.redcode.bookanddriveservice.tenants.model.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {

}
