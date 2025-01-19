package org.redcode.bookanddriveservice.tenants.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.tenants.context.TenantContext;
import org.redcode.bookanddriveservice.tenants.service.TenantsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants")
public class TenantsController {

    private final TenantsService schemaService;

    @PostMapping
    public ResponseEntity<Void> addSchemaForTenant(@RequestParam String tenantName) {
        log.info("Creating schema for tenant: {}", tenantName);
        schemaService.createDatabaseSchema(tenantName);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> getTenant() {
        return ResponseEntity.ok(TenantContext.getTenantId());
    }
}
