package org.redcode.bookanddriveservice.tenants.service;

import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.exceptions.DuplicateSchemaException;
import org.redcode.bookanddriveservice.tenants.repository.SchemaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantsService {

    private final SchemaRepository schemaRepository;

    public void createDatabaseSchema(String schemaName) {
        if (schemaRepository.doesSchemaExist(schemaName)) {
            throw DuplicateSchemaException.of("Schema " + schemaName + " already exists.", "duplicate_value");
        }
        schemaRepository.createSchema(schemaName);
    }
}
