package org.redcode.bookanddriveservice.tenants.integrationtests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITTenantsControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TransactionTemplate transactionTemplate;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void shouldReturn200WhenSchemaIsCreatedSuccessfully() {
        // Arrange: Specify a valid tenant name for the schema creation.
        String tenantName = "new_tenant";

        // Act: Call the /tenants endpoint to create a schema for the tenant.
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "/tenants?tenantName=" + tenantName,
            null,  // No body is required for this request.
            Void.class
        );

        // Assert: The response status should be 200 OK.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Optional: Verify that the schema was actually created in the database.
        Long schemaCount = (Long) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name = :schemaName"
            )
            .setParameter("schemaName", tenantName)
            .getSingleResult();
        assertThat(schemaCount).isEqualTo(1L);  // The schema should exist.
    }


    @Test
    @Transactional
    void shouldReturn400WhenSchemaAlreadyExists() {
        // Arrange: Pre-create a schema in the database for the tenant.
        String tenantName = "existing_tenant";

        // Act: Attempt to create the same schema via the /tenants endpoint.
        ResponseEntity<Map<String, Object>> response1 = restTemplate.exchange(
            "/tenants?tenantName=" + tenantName,
            HttpMethod.POST,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        ResponseEntity<Map<String, Object>> response2 = restTemplate.exchange(
            "/tenants?tenantName=" + tenantName,
            HttpMethod.POST,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        // Assert: The response status should indicate a bad request (400 BAD REQUEST).
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Assert: Validate the response body contains error details.
        Map<String, Object> errorResponse = response2.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.get("status")).isEqualTo(400);
        assertThat(errorResponse.get("message")).isEqualTo("Schema " + tenantName + " already exists.");
        assertThat(errorResponse.get("timestamp")).isNotNull(); // Optional check for timestamp.
    }
}

