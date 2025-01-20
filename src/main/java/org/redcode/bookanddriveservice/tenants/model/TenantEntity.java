package org.redcode.bookanddriveservice.tenants.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redcode.bookanddriveservice.tenants.domain.Tenant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tenant", schema = "admin")
public class TenantEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;
    private String name;

    public static TenantEntity from(Tenant tenant) {
        return TenantEntity.builder()
            .id(tenant.getId())
            .name(tenant.getName())
            .build();
    }
}
