package org.redcode.bookanddriveservice.tenants.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClerkJwtTenantResolver implements TenantResolver<HttpServletRequest> {

    @Override
    public String resolveTenantId(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            var orgClaim = jwtAuth.getToken().getClaim("o");
            if (orgClaim instanceof java.util.Map<?, ?> orgMap) {
                var orgId = String.valueOf(orgMap.get("id"));
                log.debug("Resolved tenant: {}", orgId);
                return orgId;
            }
            log.debug("JWT has no 'o' claim — no active organization in session");
            return null;
        }
        log.debug("No JwtAuthenticationToken in SecurityContext");
        return null;
    }
}
