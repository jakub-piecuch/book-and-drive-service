package org.redcode.bookanddriveservice.tenants.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.redcode.bookanddriveservice.tenants.controller.dto.CreateTenantRequest;
import org.redcode.bookanddriveservice.tenants.model.TenantEntity;

@Data
@Builder
@AllArgsConstructor
public class Tenant {
    private UUID id;
    private String name;

    public static Tenant from(TenantEntity entity) {
        return Tenant.builder()
            .id(entity.getId())
            .name(entity.getName())
            .build();
    }

    public static Tenant from(CreateTenantRequest request) {
        return Tenant.builder()
            .name(request.name())
            .build();
    }
}
