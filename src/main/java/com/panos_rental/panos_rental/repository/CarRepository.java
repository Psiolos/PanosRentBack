package com.panos_rental.panos_rental.repository;

import com.panos_rental.panos_rental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findAllByIsAvailableTrue();

    @Query("SELECT c FROM Car c WHERE c.isAvailable = true AND " +
            "c.id NOT IN (SELECT r.car.id FROM Reservation r WHERE r.status = 'confirmed' " +
            "AND r.startDate <= :endDate AND r.endDate >= :startDate)")
    List<Car> findAvailableCars(LocalDate startDate, LocalDate endDate);

    Optional<Car> findByLicensePlate(String licensePlate);

}