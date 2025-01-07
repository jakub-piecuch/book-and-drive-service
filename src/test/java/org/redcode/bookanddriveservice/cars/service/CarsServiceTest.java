package org.redcode.bookanddriveservice.cars.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.mappers.CarsMapper;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.cars.repository.CarsRepository;

class CarsServiceTest {

    @Mock
    private CarsRepository carsRepository;

    @Mock
    private CarsMapper carsMapper;

    @InjectMocks
    private CarsService carsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Car car = new Car(UUID.randomUUID(), "Toyota", "Camry", "ABC123");
        CarEntity carEntity = new CarEntity();
        CarEntity savedCarEntity = new CarEntity();

        when(carsMapper.mapToCarEntity(any(Car.class))).thenReturn(carEntity);
        when(carsRepository.save(any(CarEntity.class))).thenReturn(savedCarEntity);
        when(carsMapper.mapToCar(any(CarEntity.class))).thenReturn(car);

        Car result = carsService.create(car);

        assertNotNull(result);
        assertEquals(car, result);
    }

    @Test
    void testGetCars() {
        CarEntity carEntity = new CarEntity();
        Car car = new Car(UUID.randomUUID(), "Toyota", "Camry", "ABC123");

        when(carsRepository.findAll()).thenReturn(List.of(carEntity));
        when(carsMapper.mapToCar(any(CarEntity.class))).thenReturn(car);

        List<Car> result = carsService.getCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(car, result.get(0));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        CarEntity carEntity = new CarEntity();
        Car car = new Car(id, "Toyota", "Camry", "ABC123");

        when(carsRepository.findById(id)).thenReturn(Optional.of(carEntity));
        when(carsMapper.mapToCar(any(CarEntity.class))).thenReturn(car);

        Car result = carsService.findById(id);

        assertNotNull(result);
        assertEquals(car, result);
    }

    @Test
    void testUpdateById() {
        UUID id = UUID.randomUUID();
        Car car = new Car(id, "Toyota", "Camry", "ABC123");
        CarEntity carEntity = new CarEntity();
        CarEntity updatedCarEntity = new CarEntity();

        when(carsRepository.findById(id)).thenReturn(Optional.of(carEntity));
        when(carsMapper.mapToCarEntity(any(Car.class))).thenReturn(updatedCarEntity);
        when(carsRepository.save(any(CarEntity.class))).thenReturn(updatedCarEntity);
        when(carsMapper.mapToCar(any(CarEntity.class))).thenReturn(car);

        Car result = carsService.updateById(id, car);

        assertNotNull(result);
        assertEquals(car, result);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        CarEntity carEntity = new CarEntity();

        when(carsRepository.findById(id)).thenReturn(Optional.of(carEntity));

        UUID result = carsService.deleteById(id);

        assertNotNull(result);
        assertEquals(id, result);
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();

        when(carsRepository.existsById(id)).thenReturn(true);

        boolean result = carsService.existsById(id);

        assertTrue(result);
    }
}
