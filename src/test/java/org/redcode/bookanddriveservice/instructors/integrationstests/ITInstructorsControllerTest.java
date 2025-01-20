package org.redcode.bookanddriveservice.instructors.integrationstests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redcode.bookanddriveservice.instructors.controller.dto.CreateInstructorRequest;
import org.redcode.bookanddriveservice.instructors.controller.dto.InstructorResponse;
import org.redcode.bookanddriveservice.instructors.controller.dto.UpdateInstructorRequest;
import org.redcode.bookanddriveservice.instructors.model.InstructorEntity;
import org.redcode.bookanddriveservice.instructors.repository.InstructorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITInstructorsControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    InstructorsRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "instructor");
    }

    @Test
    void shouldFindAllInstructors() {
        addInstructor();
        List<InstructorResponse> instructors = restTemplate.exchange(
            "/instructors",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<InstructorResponse>>() {
            }).getBody();

        assertThat(instructors).isNotNull();
        assertThat(instructors.size()).isEqualTo(1);
    }

    @Test
    void shouldCreateInstructor() {
        CreateInstructorRequest request = CreateInstructorRequest.builder()
            .name("Jane")
            .sureName("Smith")
            .email("jane.smith@example.com")
            .build();

        InstructorResponse response = restTemplate.postForEntity("/instructors", request, InstructorResponse.class).getBody();

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Jane");
        assertThat(response.sureName()).isEqualTo("Smith");
        assertThat(response.email()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void shouldFindInstructorById() {
        InstructorResponse addedInstructor = addInstructor();

        InstructorResponse response = restTemplate.getForObject("/instructors/" + addedInstructor.id(), InstructorResponse.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Jane");
        assertThat(response.sureName()).isEqualTo("Smith");
        assertThat(response.email()).isEqualTo(addedInstructor.email());
    }

    @Test
    void shouldUpdateInstructorById() {
        InstructorResponse addedInstructor = addInstructor();
        UUID instructorId = addedInstructor.id();

        UpdateInstructorRequest updateRequest = UpdateInstructorRequest.builder()
            .name("UpdatedJane")
            .sureName("UpdatedSmith")
            .email("updated.jane.smith@example.com")
            .build();

        ResponseEntity<InstructorResponse> response = restTemplate.exchange(
            "/instructors/" + instructorId,
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            InstructorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        InstructorEntity updatedInstructor = entityManager.find(InstructorEntity.class, instructorId);

        assertThat(updatedInstructor.getName()).isEqualTo("UpdatedJane");
        assertThat(updatedInstructor.getSureName()).isEqualTo("UpdatedSmith");
        assertThat(updatedInstructor.getEmail()).isEqualTo("updated.jane.smith@example.com");
    }

    @Test
    void shouldDeleteInstructorById() {
        InstructorResponse addedInstructor = addInstructor();

        restTemplate.delete("/instructors/" + addedInstructor.id());

        assertThat(repository.findById(addedInstructor.id())).isEmpty();
    }

    private InstructorResponse addInstructor() {
        CreateInstructorRequest request = CreateInstructorRequest.builder()
            .name("Jane")
            .sureName("Smith")
            .email("jane.smith@example.com")
            .build();

        return restTemplate.postForEntity("/instructors", request, InstructorResponse.class).getBody();
    }

    @Test
    void shouldUpdateInstructor_throwNotFoundException() {
        UUID nonExistentInstructorId = UUID.randomUUID();

        UpdateInstructorRequest updateRequest = UpdateInstructorRequest.builder()
            .name("NonExistentFirstName")
            .sureName("NonExistentLastName")
            .email("nonexistent@example.com")
            .build();

        ResponseEntity<String> response = restTemplate.exchange(
            "/instructors/" + nonExistentInstructorId,
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Resource not found.");
    }

    @Test
    void shouldDeleteInstructor_throwNotFoundException() {
        UUID nonExistentInstructorId = UUID.randomUUID();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            "/instructors/" + nonExistentInstructorId,
            HttpMethod.DELETE,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {
            }
        );

        // Assert status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // Assert error response body
        Map<String, Object> errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.get("status")).isEqualTo(404);
        assertThat(errorResponse.get("message")).isEqualTo("Resource not found.");
        assertThat(errorResponse.get("reason")).isEqualTo("Not Found");
        assertThat(errorResponse.get("timestamp")).isNotNull();
    }
}
