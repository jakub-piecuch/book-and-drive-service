package org.redcode.bookanddriveservice.cars.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redcode.bookanddriveservice.cars.domain.Car;
import org.redcode.bookanddriveservice.cars.service.CarsService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
class CarsControllerTest {

    @Mock
    CarsService carsService; // Mock of the service

    private MockMvc mockMvc;

    @InjectMocks
    private CarsController carsController; // CarsController with mocked dependencies

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carsController).build();
    }

    @Test
    void testCreateCar() throws Exception {
        Car car = new Car(UUID.randomUUID(), "Toyota", "Camry", "ABC123");

        when(carsService.create(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"make\":\"Toyota\",\"model\":\"Camry\",\"registrationNumber\":\"ABC123\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.make").value("Toyota"))
            .andExpect(jsonPath("$.model").value("Camry"))
            .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
    }

    @Test
    void testGetCars() throws Exception {
        Car car = new Car(UUID.randomUUID(), "Toyota", "Camry", "ABC123");

        when(carsService.getCars()).thenReturn(List.of(car));

        mockMvc.perform(get("/api/cars"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].make").value("Toyota"))
            .andExpect(jsonPath("$[0].model").value("Camry"))
            .andExpect(jsonPath("$[0].registrationNumber").value("ABC123"));
    }

    @Test
    void testGetCarById() throws Exception {
        UUID id = UUID.randomUUID();
        Car car = new Car(id, "Toyota", "Camry", "ABC123");

        when(carsService.findById(id)).thenReturn(car);

        mockMvc.perform(get("/api/cars/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.make").value(car.getMake().toString()))
            .andExpect(jsonPath("$.model").value(car.getModel()))
            .andExpect(jsonPath("$.registrationNumber").value(car.getRegistrationNumber().toString()));
    }

    @Test
    void testUpdateCarById() throws Exception {
        UUID id = UUID.randomUUID();
        Car car = new Car(id, "Toyota", "Camry", "ABC123");

        when(carsService.updateById(any(UUID.class), any(Car.class))).thenReturn(car);

        mockMvc.perform(put("/api/cars/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"make\":\"Toyota\",\"model\":\"Camry\",\"registrationNumber\":\"ABC123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.make").value("Toyota"))
            .andExpect(jsonPath("$.model").value("Camry"))
            .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
    }

    @Test
    void testDeleteCarById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/cars/{id}", id))
            .andExpect(status().isNoContent());
    }
}
