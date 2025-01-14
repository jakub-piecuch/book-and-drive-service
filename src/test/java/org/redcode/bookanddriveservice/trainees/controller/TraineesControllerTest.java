package org.redcode.bookanddriveservice.trainees.controller;

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
import org.redcode.bookanddriveservice.trainees.dto.Trainee;
import org.redcode.bookanddriveservice.trainees.service.TraineesService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
class TraineesControllerTest {

    @Mock
    TraineesService traineesService; // Mock of the service

    private MockMvc mockMvc;

    @InjectMocks
    private TraineesController traineesController; // TraineesController with mocked dependencies

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineesController).build();
    }

    @Test
    void testCreateTrainee() throws Exception {
        Trainee trainee = new Trainee(UUID.randomUUID(), "Jan", "Kowalski", "abc@gmail.com");

        when(traineesService.create(any(Trainee.class))).thenReturn(trainee);

        mockMvc.perform(post("/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jan\",\"sureName\":\"Kowalski\",\"email\":\"abc@gmail.com\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Jan"))
            .andExpect(jsonPath("$.sureName").value("Kowalski"))
            .andExpect(jsonPath("$.email").value("abc@gmail.com"));
    }

    @Test
    void testGetTrainees() throws Exception {
        Trainee trainee = new Trainee(UUID.randomUUID(), "Jan", "Kowalski", "abc@gmail.com");

        when(traineesService.getTrainees()).thenReturn(List.of(trainee));

        mockMvc.perform(get("/trainees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Jan"))
            .andExpect(jsonPath("$[0].sureName").value("Kowalski"))
            .andExpect(jsonPath("$[0].email").value("abc@gmail.com"));
    }

    @Test
    void testGetTraineeById() throws Exception {
        UUID id = UUID.randomUUID();
        Trainee trainee = new Trainee(id, "Jan", "Kowalski", "abc@gmail.com");

        when(traineesService.findById(id)).thenReturn(trainee);

        mockMvc.perform(get("/trainees/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(trainee.getName()))
            .andExpect(jsonPath("$.sureName").value(trainee.getSureName()))
            .andExpect(jsonPath("$.email").value(trainee.getEmail()));
    }

    @Test
    void testGetTraineeById_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(traineesService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/trainees/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTraineeById() throws Exception {
        UUID id = UUID.randomUUID();
        Trainee trainee = new Trainee(id, "Jan", "Kowalski", "abc@gmail.com");

        when(traineesService.updateById(any(UUID.class), any(Trainee.class))).thenReturn(trainee);

        mockMvc.perform(put("/trainees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jan\",\"sureName\":\"Kowalski\",\"email\":\"abc@gmail.com\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Jan"))
            .andExpect(jsonPath("$.sureName").value("Kowalski"))
            .andExpect(jsonPath("$.email").value("abc@gmail.com"));
    }

    @Test
    void testUpdateTraineeById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        Trainee trainee = new Trainee(id, "Jan", "Kowalski", "abc@gmail.com");

        when(traineesService.updateById(any(UUID.class), any(Trainee.class))).thenReturn(null);

        mockMvc.perform(put("/trainees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jan\",\"sureName\":\"Kowalski\",\"email\":\"abc@gmail.com\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTraineeById() throws Exception {
        UUID id = UUID.randomUUID();

        when(traineesService.deleteById(any(UUID.class))).thenReturn(id);

        mockMvc.perform(delete("/trainees/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(id.toString()));
    }

    @Test
    void testDeleteTraineeById_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(traineesService.deleteById(any(UUID.class))).thenReturn(null);

        mockMvc.perform(delete("/trainees/{id}", id))
            .andExpect(status().isNotFound());
    }
}
