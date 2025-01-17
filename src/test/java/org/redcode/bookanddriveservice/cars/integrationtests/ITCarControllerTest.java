package org.redcode.bookanddriveservice.cars.integrationtests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redcode.bookanddriveservice.cars.controller.dto.CarResponse;
import org.redcode.bookanddriveservice.cars.controller.dto.CreateCarRequest;
import org.redcode.bookanddriveservice.cars.controller.dto.UpdateCarRequest;
import org.redcode.bookanddriveservice.cars.domain.Car;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.cars.repository.CarsRepository;
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
class ITCarControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CarsRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "car");
    }

    @Test
    void shouldFindAllCars() {
        addCar();
        String url = "/cars";
        List<Car> cars = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {
        }).getBody();

        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(1);
    }


    @Test
    void shouldCreateCar() {
        CreateCarRequest request = CreateCarRequest.builder()
            .make("skoda")
            .model("superb")
            .registrationNumber("SB123")
            .build();

        CarResponse response = restTemplate.postForEntity("/cars", request, CarResponse.class).getBody();

        assertThat(response.id()).isNotNull();
        assertThat(response.make()).isEqualTo("skoda");
        assertThat(response.model()).isEqualTo("superb");
        assertThat(response.registrationNumber()).isEqualTo("SB123");
    }

    @Test
    void shouldFindCarById() {
        CarResponse addedCar = addCar();
        String registrationNumber = addedCar.registrationNumber();

        CarResponse response = restTemplate.getForObject("/cars/" + addedCar.id(), CarResponse.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.make()).isEqualTo("skoda");
        assertThat(response.model()).isEqualTo("superb");
        assertThat(response.registrationNumber()).isEqualTo(registrationNumber);
    }

    @Test
    void shouldUpdateCarId() {
        // Arrange: Create a car and capture its ID
        CarResponse addedCar = addCar();
        UUID carId = addedCar.id();

        // Create an update request
        UpdateCarRequest updateRequest = UpdateCarRequest.builder()
            .make("updated-make")
            .model("updated-model")
            .registrationNumber("UPDATED123")
            .build();

        // Act: Perform the PUT request
        ResponseEntity<CarResponse> response = restTemplate.exchange(
            "/cars/" + carId,
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            CarResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Assert: Retrieve the updated car and validate changes
        var updatedCar = entityManager.find(CarEntity.class, carId);

        assertThat(updatedCar.getId()).isEqualTo(carId);
        assertThat(updatedCar.getMake()).isEqualTo("updated-make");
        assertThat(updatedCar.getModel()).isEqualTo("updated-model");
        assertThat(updatedCar.getRegistrationNumber()).isEqualTo("UPDATED123");
    }

    @Test
    void shouldUpdateCar_throwNotFoundException() {
        UUID carId = UUID.randomUUID();

        // Create an update request
        UpdateCarRequest updateRequest = UpdateCarRequest.builder()
            .make("updated-make")
            .model("updated-model")
            .registrationNumber("UPDATED123")
            .build();

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/cars/" + carId,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Resource not found."); // Check custom error details
    }


    @Test
    void shouldDeleteCarById() {
        CarResponse response = addCar();

        restTemplate.delete("/cars/" + response.id());

        var result = repository.findById(response.id());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldDeleteCarById_throwsException() {
        UUID nonExistentCarId = UUID.randomUUID();

        var response = restTemplate.exchange(
            "/cars/" + nonExistentCarId,
            org.springframework.http.HttpMethod.DELETE,
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

    private CarResponse addCar() {
        CreateCarRequest request = CreateCarRequest.builder()
            .make("skoda")
            .model("superb")
            .registrationNumber(UUID.randomUUID().toString())
            .build();

        return restTemplate.postForEntity("/cars", request, CarResponse.class).getBody();
    }
}
