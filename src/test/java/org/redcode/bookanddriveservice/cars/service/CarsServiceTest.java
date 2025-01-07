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
import org.mockito.Spy;
import org.redcode.bookanddriveservice.cars.dto.Car;
import org.redcode.bookanddriveservice.cars.mappers.CarsMapper;
import org.redcode.bookanddriveservice.cars.model.CarEntity;
import org.redcode.bookanddriveservice.cars.repository.CarsRepository;
import org.redcode.bookanddriveservice.cars.utils.DataGenerator;

class CarsServiceTest {

    @Mock
    private CarsRepository carsRepository;

    @Spy
    private CarsMapper carsMapper = new CarsMapper();

    @InjectMocks
    private CarsService carsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Car car = DataGenerator.generateCar();
        CarEntity savedCarEntity = DataGenerator.generateCarEntity();

        when(carsRepository.save(any(CarEntity.class))).thenReturn(savedCarEntity);

        Car result = carsService.create(car);

        assertNotNull(result);
        assertEquals(car, result);
    }

    @Test
    void testGetCars() {
        CarEntity carEntity = DataGenerator.generateCarEntity();
        Car car = DataGenerator.generateCar();

        when(carsRepository.findAll()).thenReturn(List.of(carEntity));

        List<Car> result = carsService.getCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(car, result.get(0));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Car car = DataGenerator.generateCar();
        CarEntity carEntity = DataGenerator.generateCarEntity();

        when(carsRepository.findById(id)).thenReturn(Optional.of(carEntity));

        Car result = carsService.findById(id);

        assertNotNull(result);
        assertEquals(car, result);
    }

    @Test
    void testUpdateById() {
        UUID id = UUID.randomUUID();
        Car car = DataGenerator.generateCar();
        Car updatedCar = car;
        CarEntity carEntity = DataGenerator.generateCarEntity();
        CarEntity updatedCarEntity = carEntity;
        updatedCar.setModel("Yaris");
        updatedCarEntity.setModel("Yaris");

        when(carsRepository.findById(id)).thenReturn(Optional.of(carEntity));
        when(carsRepository.save(any(CarEntity.class))).thenReturn(updatedCarEntity);

        Car result = carsService.updateById(id, car);

        assertNotNull(result);
        assertEquals(updatedCar, result);
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
