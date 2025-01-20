package org.redcode.bookanddriveservice.tenants.controller.dto;

import java.util.UUID;
import lombok.Builder;
import org.redcode.bookanddriveservice.tenants.domain.Tenant;

@Builder
public record TenantResponse(
    UUID id,
    String name
) {
    public static TenantResponse from(Tenant tenant) {
        return TenantResponse.builder()
            .id(tenant.getId())
            .name(tenant.getName())
            .build();
    }
}
