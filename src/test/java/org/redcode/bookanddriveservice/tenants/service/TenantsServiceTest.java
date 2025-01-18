package org.redcode.bookanddriveservice.tenants.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.redcode.bookanddriveservice.exceptions.DuplicateSchemaException;
import org.redcode.bookanddriveservice.tenants.repository.SchemaRepository;

class TenantsServiceTest {

    private final SchemaRepository schemaRepository = mock(SchemaRepository.class);
    private final TenantsService tenantsService = new TenantsService(schemaRepository);

    @Test
    void shouldCreateSchemaWhenSchemaDoesNotExist() { // Happy path case
        // Arrange
        String schemaName = "new_schema";
        when(schemaRepository.doesSchemaExist(schemaName)).thenReturn(false);

        // Act
        tenantsService.createDatabaseSchema(schemaName);

        // Assert
        verify(schemaRepository, times(1)).createSchema(schemaName); // Expect creation to happen
        verify(schemaRepository, times(1)).doesSchemaExist(schemaName); // Check schema existence query
    }

    @Test
    void shouldThrowExceptionWhenSchemaAlreadyExists() { // Schema already exists case
        // Arrange
        String schemaName = "existing_schema";
        when(schemaRepository.doesSchemaExist(schemaName)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> tenantsService.createDatabaseSchema(schemaName))
            .isInstanceOf(DuplicateSchemaException.class)
            .hasMessage("Schema existing_schema already exists.");

        verify(schemaRepository, times(1)).doesSchemaExist(schemaName); // Check schema existence
        verify(schemaRepository, never()).createSchema(schemaName); // Creation should NOT happen
    }
}
