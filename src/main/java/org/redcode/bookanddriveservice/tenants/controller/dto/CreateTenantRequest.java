package org.redcode.bookanddriveservice.tenants.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTenantRequest(
    @NotNull
    String name
) {

}
