package org.redcode.bookanddriveservice.tenants.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.redcode.bookanddriveservice.exceptions.ValidationException;
import org.springframework.stereotype.Repository;

@Repository
public class SchemaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createSchema(String schemaName) {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw ValidationException.of("Schema name cannot be null or empty", "empty_value");
        }

        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
