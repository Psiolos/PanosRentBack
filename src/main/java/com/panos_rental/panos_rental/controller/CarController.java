package com.panos_rental.panos_rental.controller;

import com.panos_rental.panos_rental.entity.Car;
import com.panos_rental.panos_rental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;


    @PostMapping("/add")
    public ResponseEntity<Car> addCarWithPhoto(
            @ModelAttribute Car car,
            @RequestParam("photo") MultipartFile photo) {
        try {
            Car savedCar = carService.saveCarWithPhoto(car, photo);
            return ResponseEntity.ok(savedCar);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/all-cars")
    public ResponseEntity<List<Car>> findAllCars() {
        List<Car> cars = carService.findAllCars();
        return ResponseEntity.ok(cars);
    }


    @PutMapping("/{carId}/price")
    public ResponseEntity<Car> updateCarPrice(@PathVariable int carId, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal newPrice = request.get("price");
        Optional<Car> updatedCar = carService.updateCarPrice(carId, newPrice);
        return updatedCar.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{carId}/availability")
    public ResponseEntity<Car> toggleCarAvailability(@PathVariable int carId) {
        Car car = carService.findById(carId);
        if (car != null) {

            car.setAvailable(!car.getAvailable());
            carService.saveCar(car);
            return ResponseEntity.ok(car);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable int carId) {
        boolean deleted = carService.deleteCar(carId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/retire")
    public ResponseEntity<Void> retireCar(@PathVariable int id) {
        carService.updateCarStatus(id, "retired");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-by-license-plate/{licensePlate}")
    public ResponseEntity<Car> getCarByLicensePlate(@PathVariable String licensePlate) {
        Optional<Car> carOptional = carService.findByLicensePlate(licensePlate);
        if (carOptional.isPresent()) {
            return ResponseEntity.ok(carOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
