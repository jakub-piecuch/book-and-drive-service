package org.redcode.bookanddriveservice.migration.provider;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.redcode.bookanddriveservice.migration.config.DatasourceConnectionConfig;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MigrationProvider {

    public static final String CLASSPATH_DB_MIGRATION_TENANTS = "classpath:db/migration/tenants";

    private final DatasourceConnectionConfig datasourceConfig;

    public Flyway configureFlywayFor(String schema) {
        String url = datasourceConfig.getUrl();
        String username = datasourceConfig.getUsername();
        String password = datasourceConfig.getPassword();

        return Flyway.configure()
            .dataSource(url, username, password)
            .locations(CLASSPATH_DB_MIGRATION_TENANTS)
            .schemas(schema)
            .load();
    }
}
