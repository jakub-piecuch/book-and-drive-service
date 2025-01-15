package org.redcode.bookanddriveservice.instructors.controller;

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
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.instructors.service.InstructorsService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
class InstructorsControllerTest {

    @Mock
    InstructorsService instructorsService; // Mock of the service

    private MockMvc mockMvc;

    @InjectMocks
    private IntructorsController instructorsController; // InstructorsController with mocked dependencies

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(instructorsController).build();
    }

    @Test
    void testCreateInstructor() throws Exception {
        Instructor instructor = new Instructor(UUID.randomUUID(), "Jan", "Kowalski", "abc@gmail.com");

        when(instructorsService.create(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(post("/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jan\",\"sureName\":\"Kowalski\",\"email\":\"abc@gmail.com\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Jan"))
            .andExpect(jsonPath("$.sureName").value("Kowalski"))
            .andExpect(jsonPath("$.email").value("abc@gmail.com"));
    }

    @Test
    void testGetInstructors() throws Exception {
        Instructor instructor = new Instructor(UUID.randomUUID(), "Jan", "Kowalski", "abc@gmail.com");

        when(instructorsService.getInstructors()).thenReturn(List.of(instructor));

        mockMvc.perform(get("/instructors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Jan"))
            .andExpect(jsonPath("$[0].sureName").value("Kowalski"))
            .andExpect(jsonPath("$[0].email").value("abc@gmail.com"));
    }

    @Test
    void testGetInstructorById() throws Exception {
        UUID id = UUID.randomUUID();
        Instructor instructor = new Instructor(id, "Jan", "Kowalski", "abc@gmail.com");

        when(instructorsService.findById(id)).thenReturn(instructor);

        mockMvc.perform(get("/instructors/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(instructor.getName()))
            .andExpect(jsonPath("$.sureName").value(instructor.getSureName()))
            .andExpect(jsonPath("$.email").value(instructor.getEmail()));
    }

    @Test
    void testGetInstructorById_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(instructorsService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/instructors/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateInstructorById() throws Exception {
        UUID id = UUID.randomUUID();
        Instructor instructor = new Instructor(id, "Jan", "Kowalski", "abc@gmail.com");

        when(instructorsService.updateById(any(UUID.class), any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(put("/instructors/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jan\",\"sureName\":\"Kowalski\",\"email\":\"abc@gmail.com\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Jan"))
            .andExpect(jsonPath("$.sureName").value("Kowalski"))
            .andExpect(jsonPath("$.email").value("abc@gmail.com"));
    }

    @Test
    void testUpdateInstructorById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        Instructor instructor = new Instructor(id, "Jan", "Kowalski", "abc@gmail.com");

        when(instructorsService.updateById(any(UUID.class), any(Instructor.class))).thenReturn(null);

        mockMvc.perform(put("/instructors/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jan\",\"sureName\":\"Kowalski\",\"email\":\"abc@gmail.com\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInstructorById() throws Exception {
        UUID id = UUID.randomUUID();

        when(instructorsService.deleteById(any(UUID.class))).thenReturn(id);

        mockMvc.perform(delete("/instructors/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(id.toString()));
    }

    @Test
    void testDeleteInstructorById_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(instructorsService.deleteById(any(UUID.class))).thenReturn(null);

        mockMvc.perform(delete("/instructors/{id}", id))
            .andExpect(status().isNotFound());
    }
}
