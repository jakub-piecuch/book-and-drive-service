package org.redcode.bookanddriveservice.tenants.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.exceptions.DuplicateResourceException;
import org.redcode.bookanddriveservice.migration.provider.MigrationProvider;
import org.redcode.bookanddriveservice.tenants.domain.Tenant;
import org.redcode.bookanddriveservice.tenants.model.TenantEntity;
import org.redcode.bookanddriveservice.tenants.repository.SchemaRepository;
import org.redcode.bookanddriveservice.tenants.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantsService {

    public static final String DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT = "duplicate key value violates unique constraint";

    private final SchemaRepository schemaRepository;
    private final TenantRepository tenantRepository;
    private final MigrationProvider migrationProvider;

    public Tenant createTenant(Tenant tenant) {
        String schemaName = tenant.getName();
        TenantEntity tenantEntity = TenantEntity.from(tenant);

        try {

            tenantRepository.save(tenantEntity);
            schemaRepository.createSchema(schemaName);
            migrationProvider.configureFlywayFor(schemaName).migrate();

        } catch (Exception e) {
            if (e.getCause().toString().contains(DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT)) {
                throw DuplicateResourceException.of("Tenant " + schemaName + " already exists.", "duplicate_value");
            }
        }

        return Tenant.from(tenantEntity);
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll().stream()
            .map(Tenant::from)
            .toList();
    }

}
