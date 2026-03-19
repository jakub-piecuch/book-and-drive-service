package org.redcode.bookanddriveservice.tenants.interceptor;

import io.micrometer.common.KeyValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redcode.bookanddriveservice.tenants.context.TenantContext;
import org.redcode.bookanddriveservice.tenants.resolver.ClerkJwtTenantResolver;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {

    private final ClerkJwtTenantResolver tenantResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var tenantId = tenantResolver.resolveTenantId(request);

        if (tenantId == null) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "No organization context in token. Activate an organization in your Clerk session.");
            return false;
        }

        TenantContext.setTenantId(tenantId);
        MDC.put("tenantId", tenantId);

        ServerHttpObservationFilter.findObservationContext(request).ifPresent(context ->
            context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", tenantId))
        );

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        clear();
    }

    private void clear() {
        MDC.clear();
        TenantContext.clear();
    }
}
