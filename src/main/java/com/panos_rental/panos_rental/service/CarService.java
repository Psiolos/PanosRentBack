package com.panos_rental.panos_rental.service;

import com.panos_rental.panos_rental.entity.Car;
import com.panos_rental.panos_rental.entity.CarStatus;
import com.panos_rental.panos_rental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public List<Car> findAllAvailableCars() {
        return carRepository.findAllByIsAvailableTrue();
    }


    public List<Car> findAvailableCars(LocalDate startDate, LocalDate endDate) {
        return carRepository.findAvailableCars(startDate, endDate);
    }

    public List<Car> findAllCars() {
        return carRepository.findAll(); // Βεβαιωθείτε ότι η findAll() επιστρέφει όλα τα αυτοκίνητα
    }

    public Car findById(int id) {
        return carRepository.findById(id).orElse(null);
    }

    private static final String UPLOAD_DIR = "uploads";


    public Car saveCarWithPhoto(Car car, MultipartFile photo) throws IOException {
        if (!photo.isEmpty()) {
            String fileName = car.getLicensePlate() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, photo.getBytes());
            car.setPhotoPath(filePath.toString());
        }
        return carRepository.save(car);
    }


    public Optional<Car> updateCarPrice(int carId, BigDecimal newPrice) {
        Optional<Car> carOpt = carRepository.findById(carId);
        carOpt.ifPresent(car -> {
            car.setPrice(newPrice);
            carRepository.save(car);
        });
        return carOpt;
    }


    public Optional<Car> updateCarAvailability(int carId, boolean isAvailable) {
        Optional<Car> carOpt = carRepository.findById(carId);
        carOpt.ifPresent(car -> {
            car.setAvailable(isAvailable);
            carRepository.save(car);
        });
        return carOpt;
    }

    // Διαγραφή αυτοκινήτου χωρίς διαγραφή των κρατήσεων
    public boolean deleteCar(int carId) {
        if (carRepository.existsById(carId)) {
            carRepository.deleteById(carId);
            return true;
        }
        return false;
    }

    public void updateCarStatus(int id, String status) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with ID " + id + " not found"));

        car.setStatus(CarStatus.retired); // Υποθέτουμε ότι το πεδίο `status` υπάρχει ως String ή Enum στην οντότητα Car
        carRepository.save(car);
    }

    public Optional<Car> findByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }
    // Μπορείς να προσθέσεις περισσότερες μεθόδους ανάλογα με τις ανάγκες σου
}
