package org.redcode.bookanddriveservice.tenants.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redcode.bookanddriveservice.exceptions.DuplicateResourceException;
import org.redcode.bookanddriveservice.tenants.domain.Tenant;
import org.redcode.bookanddriveservice.tenants.model.TenantEntity;
import org.redcode.bookanddriveservice.tenants.repository.TenantRepository;
import org.springframework.dao.DataIntegrityViolationException;

class TenantsServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private TenantsService tenantsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTenant_Success() {
        Tenant tenant = new Tenant(UUID.randomUUID(), "tenant1");
        TenantEntity tenantEntity = TenantEntity.from(tenant);

        when(tenantRepository.save(any())).thenReturn(tenantEntity);

        Tenant result = tenantsService.createTenant(tenant);

        assertNotNull(result);
        assertEquals(tenant.getName(), result.getName());
        verify(tenantRepository).save(any(TenantEntity.class));
    }

    @Test
    void testCreateTenant_DuplicateTenantException() {
        Tenant tenant = new Tenant(UUID.randomUUID(), "tenant1");

        RuntimeException exception = new RuntimeException(new Throwable(
            TenantsService.DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT));

        doThrow(exception).when(tenantRepository).save(any(TenantEntity.class));

        DuplicateResourceException thrown = assertThrows(DuplicateResourceException.class,
            () -> tenantsService.createTenant(tenant));

        assertNotNull(thrown);
        assertEquals("Tenant tenant1 already exists.", thrown.getMessage());
        assertEquals("duplicate_value", thrown.getReason());
        verify(tenantRepository).save(any(TenantEntity.class));
    }

    @Test
    void testCreateTenant_GeneralException() {
        Tenant tenant = new Tenant(UUID.randomUUID(), "tenant1");

        DataIntegrityViolationException generalException = new DataIntegrityViolationException(
            "Some unexpected error",
            new Throwable("duplicate key value violates unique constraint")
        );

        doThrow(generalException).when(tenantRepository).save(any(TenantEntity.class));

        RuntimeException thrown = assertThrows(RuntimeException.class,
            () -> tenantsService.createTenant(tenant));

        assertNotNull(thrown);
        assertEquals("Tenant tenant1 already exists.", thrown.getMessage());
        verify(tenantRepository).save(any(TenantEntity.class));
    }

    @Test
    void testGetAllTenants_Success() {
        TenantEntity tenant1 = new TenantEntity(UUID.randomUUID(), "tenant1");
        TenantEntity tenant2 = new TenantEntity(UUID.randomUUID(), "tenant2");

        when(tenantRepository.findAll()).thenReturn(List.of(tenant1, tenant2));

        List<Tenant> tenants = tenantsService.getAllTenants();

        assertNotNull(tenants);
        assertEquals(2, tenants.size());
        assertEquals("tenant1", tenants.get(0).getName());
        assertEquals("tenant2", tenants.get(1).getName());
        verify(tenantRepository).findAll();
    }

    @Test
    void testGetAllTenants_EmptyList() {
        when(tenantRepository.findAll()).thenReturn(List.of());

        List<Tenant> tenants = tenantsService.getAllTenants();

        assertNotNull(tenants);
        assertTrue(tenants.isEmpty());
        verify(tenantRepository).findAll();
    }
}
