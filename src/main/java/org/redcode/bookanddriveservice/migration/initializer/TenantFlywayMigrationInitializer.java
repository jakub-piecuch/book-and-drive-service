package org.redcode.bookanddriveservice.migration.initializer;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.redcode.bookanddriveservice.tenants.service.TenantsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantFlywayMigrationInitializer implements InitializingBean, Ordered {

    private static final String TENANT_MIGRATION_LOCATION = "db/migration/tenants";

    private final DataSource dataSource;
    private final Flyway defaultFlyway;
    private final TenantsService tenantsService;


    @Override
    public void afterPropertiesSet() {
        tenantsService.getAllTenants().forEach(tenant -> {
            Flyway flyway = tenantFlyway(tenant.getName());
            flyway.migrate();
        });
    }

    private Flyway tenantFlyway(String schema) {
        return Flyway.configure()
            .configuration(defaultFlyway.getConfiguration())
            .locations(TENANT_MIGRATION_LOCATION)
            .dataSource(dataSource)
            .schemas(schema)
            .load();
    }

    @Override
    public int getOrder() {
        // Executed after the default schema initialization in FlywayMigrationInitializer.
        return 1;
    }

}
