package org.redcode.bookanddriveservice.tenants.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.tenants.config.TenantHttpProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {

    private final TenantHttpProperties tenantHttpProperties;

    @Override
    public String resolveTenantId(HttpServletRequest request) {
        return request.getHeader(tenantHttpProperties.getHeader());
    }
}
