package org.redcode.bookanddriveservice.tenants.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redcode.bookanddriveservice.exceptions.DuplicateResourceException;
import org.redcode.bookanddriveservice.migration.provider.MigrationProvider;
import org.redcode.bookanddriveservice.tenants.domain.Tenant;
import org.redcode.bookanddriveservice.tenants.model.TenantEntity;
import org.redcode.bookanddriveservice.tenants.repository.SchemaRepository;
import org.redcode.bookanddriveservice.tenants.repository.TenantRepository;
import org.springframework.dao.DataIntegrityViolationException;

class TenantsServiceTest {

    @Mock
    private SchemaRepository schemaRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private MigrationProvider migrationProvider;

    @InjectMocks
    private TenantsService tenantsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTenant_Success() {
        // Arrange
        Tenant tenant = new Tenant(UUID.randomUUID(), "tenant1");
        TenantEntity tenantEntity = TenantEntity.from(tenant);

        // Mock tenant repository save method
        when(tenantRepository.save(any())).thenReturn(tenantEntity);

        // Mock schema repository createSchema (void method)
        doNothing().when(schemaRepository).createSchema(tenant.getName());

        // Mock the Flyway instance returned by configureFlywayFor
        Flyway flywayMock = mock(Flyway.class);
        when(migrationProvider.configureFlywayFor(tenant.getName())).thenReturn(flywayMock);

        // Act
        Tenant result = tenantsService.createTenant(tenant);

        // Assert
        assertNotNull(result);
        assertEquals(tenant.getName(), result.getName());

        // Verify interactions
        verify(tenantRepository).save(any(TenantEntity.class));
        verify(schemaRepository).createSchema(tenant.getName());
        verify(migrationProvider).configureFlywayFor(tenant.getName());
        verify(flywayMock).migrate();
    }

    @Test
    void testCreateTenant_DuplicateTenantException() {
        // Arrange
        Tenant tenant = new Tenant(UUID.randomUUID(), "tenant1");
        TenantEntity tenantEntity = TenantEntity.from(tenant);

        // Simulate duplicate key exception by mocking behavior
        RuntimeException exception = new RuntimeException(new Throwable(
            TenantsService.DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT));

        doThrow(exception).when(tenantRepository).save(any(TenantEntity.class));

        // Act & Assert
        DuplicateResourceException thrown = assertThrows(DuplicateResourceException.class,
            () -> tenantsService.createTenant(tenant));

        assertNotNull(thrown);
        assertEquals("Tenant tenant1 already exists.", thrown.getMessage());
        assertEquals("duplicate_value", thrown.getReason());
        verify(tenantRepository).save(any(TenantEntity.class));
        verifyNoInteractions(schemaRepository);
        verifyNoInteractions(migrationProvider);
    }

    @Test
    void testCreateTenant_GeneralException() {
        // Arrange
        Tenant tenant = new Tenant(UUID.randomUUID(), "tenant1");
        TenantEntity tenantEntity = TenantEntity.from(tenant);

        // Simulate some general exception unrelated to duplicate keys
        DataIntegrityViolationException generalException = new DataIntegrityViolationException(
            "Some unexpected error",
            new Throwable("duplicate key value violates unique constraint")
        );

        doThrow(generalException).when(tenantRepository).save(any(TenantEntity.class));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class,
            () -> tenantsService.createTenant(tenant));

        assertNotNull(thrown);
        assertEquals("Tenant tenant1 already exists.", thrown.getMessage());
        verify(tenantRepository).save(any(TenantEntity.class));
        verifyNoInteractions(schemaRepository);
        verifyNoInteractions(migrationProvider);
    }

    @Test
    void testGetAllTenants_Success() {
        // Arrange

        TenantEntity tenant1 = new TenantEntity(UUID.randomUUID(), "tenant1");
        TenantEntity tenant2 = new TenantEntity(UUID.randomUUID(), "tenant2");

        when(tenantRepository.findAll()).thenReturn(List.of(tenant1, tenant2));

        // Act
        List<Tenant> tenants = tenantsService.getAllTenants();

        // Assert
        assertNotNull(tenants);
        assertEquals(2, tenants.size());
        assertEquals("tenant1", tenants.get(0).getName());
        assertEquals("tenant2", tenants.get(1).getName());
        verify(tenantRepository).findAll();
    }

    @Test
    void testGetAllTenants_EmptyList() {
        // Arrange
        when(tenantRepository.findAll()).thenReturn(List.of());

        // Act
        List<Tenant> tenants = tenantsService.getAllTenants();

        // Assert
        assertNotNull(tenants);
        assertTrue(tenants.isEmpty());
        verify(tenantRepository).findAll();
    }
}
