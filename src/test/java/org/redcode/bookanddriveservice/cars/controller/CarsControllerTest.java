//package org.redcode.bookanddriveservice.cars.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//import java.util.UUID;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.redcode.bookanddriveservice.cars.dto.Car;
//import org.redcode.bookanddriveservice.cars.dto.CarResponse;
//import org.redcode.bookanddriveservice.cars.dto.CreateCarRequest;
//import org.redcode.bookanddriveservice.cars.dto.UpdateCarRequest;
//import org.redcode.bookanddriveservice.cars.mappers.CarsMapper;
//import org.redcode.bookanddriveservice.cars.service.CarsService;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
//class CarsControllerTest {
//
//    CarsService carsService; // Mock of the service
//
//    @Mock
//    private CarsMapper carsMapper; // Mock of the mapper
//
//    private MockMvc mockMvc;
//
//    private CarsController carsController; // CarsController with mocked dependencies
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(carsController).build();
//        carsController = new CarsController(carsService, carsMapper);
//    }
//
//    @Test
//    void testCreateCar() throws Exception {
//        CreateCarRequest request = new CreateCarRequest("Toyota", "Camry", "ABC123");
//        Car car = new Car(UUID.randomUUID(), "Toyota", "Camry", "ABC123");
//        CarResponse response = new CarResponse(car.getId(), car.getMake(), car.getModel(), car.getRegistrationNumber());
//
//        when(carsMapper.mapToCar(any(CreateCarRequest.class))).thenReturn(car);
//        when(carsService.create(any(Car.class))).thenReturn(car);
//        when(carsMapper.mapToCarResponse(any(Car.class))).thenReturn(response);
//
//        mockMvc.perform(post("/cars")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"make\":\"Toyota\",\"model\":\"Camry\",\"registrationNumber\":\"ABC123\"}"))
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.make").value("Toyota"))
//            .andExpect(jsonPath("$.model").value("Camry"))
//            .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
//    }
//
//    @Test
//    void testGetCars() throws Exception {
//        Car car = new Car(UUID.randomUUID(), "Toyota", "Camry", "ABC123");
//        CarResponse response = new CarResponse(car.getId(), car.getMake(), car.getModel(), car.getRegistrationNumber());
//
//        when(carsService.getCars()).thenReturn(List.of(car));
//        when(carsMapper.mapToCarResponse(any(Car.class))).thenReturn(response);
//
//        mockMvc.perform(get("/cars"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].make").value("Toyota"))
//            .andExpect(jsonPath("$[0].model").value("Camry"))
//            .andExpect(jsonPath("$[0].registrationNumber").value("ABC123"));
//    }
//
//    @Test
//    void testGetCarById() throws Exception {
//        UUID id = UUID.randomUUID();
//        Car car = new Car(id, "Toyota", "Camry", "ABC123");
//        CarResponse response = new CarResponse(car.getId(), car.getMake(), car.getModel(), car.getRegistrationNumber());
//
//        when(carsService.findById(id)).thenReturn(car);
//        when(carsMapper.mapToCarResponse(any(Car.class))).thenReturn(response);
//
//        mockMvc.perform(get("/cars/{id}", id))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.make").value("Toyota"))
//            .andExpect(jsonPath("$.model").value("Camry"))
//            .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
//    }
//
//    @Test
//    void testUpdateCarById() throws Exception {
//        UUID id = UUID.randomUUID();
//        UpdateCarRequest request = new UpdateCarRequest("Toyota", "Camry", "ABC123");
//        Car car = new Car(id, "Toyota", "Camry", "ABC123");
//        CarResponse response = new CarResponse(car.getId(), car.getMake(), car.getModel(), car.getRegistrationNumber());
//
//        when(carsMapper.mapToCar(any(UpdateCarRequest.class))).thenReturn(car);
//        when(carsService.updateById(any(UUID.class), any(Car.class))).thenReturn(car);
//        when(carsMapper.mapToCarResponse(any(Car.class))).thenReturn(response);
//
//        mockMvc.perform(put("/cars/{id}", id)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"make\":\"Toyota\",\"model\":\"Camry\",\"registrationNumber\":\"ABC123\"}"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.make").value("Toyota"))
//            .andExpect(jsonPath("$.model").value("Camry"))
//            .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
//    }
//
//    @Test
//    void testDeleteCarById() throws Exception {
//        UUID id = UUID.randomUUID();
//
//        when(carsService.existsById(id)).thenReturn(true);
//
//        mockMvc.perform(delete("/cars/{id}", id))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$").value(id.toString()));
//    }
//}
