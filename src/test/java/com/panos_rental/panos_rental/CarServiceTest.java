package com.panos_rental.panos_rental;

import com.panos_rental.panos_rental.entity.Car;
import com.panos_rental.panos_rental.entity.CarStatus;
import com.panos_rental.panos_rental.repository.CarRepository;
import com.panos_rental.panos_rental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        car = new Car();
        car.setId(1);
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setYear(2020);
        car.setLicensePlate("ABC-1234");
        car.setPrice(new BigDecimal("50.00"));
        car.setAvailable(true);
        car.setStatus(CarStatus.active);
    }

    @Test
    public void testSaveCar() {

        when(carRepository.save(any(Car.class))).thenReturn(car);


        Car savedCar = carService.saveCar(car);


        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getBrand());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void testFindAllAvailableCars() {

        when(carRepository.findAllByIsAvailableTrue()).thenReturn(Arrays.asList(car));


        List<Car> availableCars = carService.findAllAvailableCars();


        assertFalse(availableCars.isEmpty());
        assertEquals(1, availableCars.size());
        assertTrue(availableCars.get(0).getAvailable());
        verify(carRepository, times(1)).findAllByIsAvailableTrue();
    }

    @Test
    public void testUpdateCarPrice() {

        BigDecimal newPrice = new BigDecimal("60.00");
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);


        Optional<Car> updatedCarOpt = carService.updateCarPrice(1, newPrice);


        assertTrue(updatedCarOpt.isPresent());
        assertEquals(newPrice, updatedCarOpt.get().getPrice());
        verify(carRepository, times(1)).findById(1);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void testUpdateCarAvailability() {

        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);


        Optional<Car> updatedCarOpt = carService.updateCarAvailability(1, false);


        assertTrue(updatedCarOpt.isPresent());
        assertFalse(updatedCarOpt.get().getAvailable());
        verify(carRepository, times(1)).findById(1);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void testDeleteCar() {

        when(carRepository.existsById(1)).thenReturn(true);
        doNothing().when(carRepository).deleteById(1);


        boolean result = carService.deleteCar(1);


        assertTrue(result);
        verify(carRepository, times(1)).existsById(1);
        verify(carRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSaveCarWithPhoto() throws IOException {

        MultipartFile photoMock = mock(MultipartFile.class);
        when(photoMock.isEmpty()).thenReturn(false);
        when(photoMock.getOriginalFilename()).thenReturn("car_photo.jpg");
        when(carRepository.save(any(Car.class))).thenReturn(car);


        Car savedCar = carService.saveCarWithPhoto(car, photoMock);


        assertNotNull(savedCar);
        assertTrue(savedCar.getPhotoPath().contains("car_photo.jpg"));
        verify(carRepository, times(1)).save(car);
    }
}
