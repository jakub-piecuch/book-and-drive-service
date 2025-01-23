package org.redcode.bookanddriveservice.trainees.integrationtests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redcode.bookanddriveservice.trainees.controller.dto.CreateTraineeRequest;
import org.redcode.bookanddriveservice.trainees.controller.dto.TraineeResponse;
import org.redcode.bookanddriveservice.trainees.controller.dto.UpdateTraineeRequest;
import org.redcode.bookanddriveservice.trainees.model.TraineeEntity;
import org.redcode.bookanddriveservice.trainees.repository.TraineesRepository;
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
class ITTraineeControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TraineesRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "trainee");
    }

    @Test
    void shouldFindAllTrainees() {
        addTrainee();
        List<TraineeResponse> trainees = restTemplate.exchange(
            "/api/trainees",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TraineeResponse>>() {
            }).getBody();

        assertThat(trainees).isNotNull();
        assertThat(trainees.size()).isEqualTo(1);
    }

    @Test
    void shouldCreateTrainee() {
        CreateTraineeRequest request = CreateTraineeRequest.builder()
            .name("John")
            .sureName("Doe")
            .email("john.doe@example.com")
            .build();

        TraineeResponse response = restTemplate.postForEntity("/api/trainees", request, TraineeResponse.class).getBody();

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("John");
        assertThat(response.sureName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo("john.doe@example.com");
    }

    @Test
    void shouldFindTraineeById() {
        TraineeResponse addedTrainee = addTrainee();

        TraineeResponse response = restTemplate.getForObject("/api/trainees/" + addedTrainee.id(), TraineeResponse.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("John");
        assertThat(response.sureName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo(addedTrainee.email());
    }

    @Test
    void shouldUpdateTraineeById() {
        TraineeResponse addedTrainee = addTrainee();
        UUID traineeId = addedTrainee.id();

        UpdateTraineeRequest updateRequest = UpdateTraineeRequest.builder()
            .name("UpdatedJohn")
            .sureName("UpdatedDoe")
            .email("updated.john.doe@example.com")
            .build();

        ResponseEntity<TraineeResponse> response = restTemplate.exchange(
            "/api/trainees/" + traineeId,
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            TraineeResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TraineeEntity updatedTrainee = entityManager.find(TraineeEntity.class, traineeId);

        assertThat(updatedTrainee.getName()).isEqualTo("UpdatedJohn");
        assertThat(updatedTrainee.getSureName()).isEqualTo("UpdatedDoe");
        assertThat(updatedTrainee.getEmail()).isEqualTo("updated.john.doe@example.com");
    }

    @Test
    void shouldDeleteTraineeById() {
        TraineeResponse addedTrainee = addTrainee();

        restTemplate.delete("/api/trainees/" + addedTrainee.id());

        assertThat(repository.findById(addedTrainee.id())).isEmpty();
    }

    private TraineeResponse addTrainee() {
        CreateTraineeRequest request = CreateTraineeRequest.builder()
            .name("John")
            .sureName("Doe")
            .email("john.doe@example.com")
            .build();

        return restTemplate.postForEntity("/api/trainees", request, TraineeResponse.class).getBody();
    }

    @Test
    void shouldUpdateTrainee_throwNotFoundException() {
        UUID nonExistentTraineeId = UUID.randomUUID();

        UpdateTraineeRequest updateRequest = UpdateTraineeRequest.builder()
            .name("NonExistentFirstName")
            .sureName("NonExistentLastName")
            .email("nonexistent@example.com")
            .build();

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/trainees/" + nonExistentTraineeId,
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Resource not found.");
    }

    @Test
    void shouldDeleteTrainee_throwNotFoundException() {
        UUID nonExistentTraineeId = UUID.randomUUID();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            "/api/trainees/" + nonExistentTraineeId,
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
