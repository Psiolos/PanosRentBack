package com.panos_rental.panos_rental.repository;

import com.panos_rental.panos_rental.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByClientId(int clientId);
    List<Reservation> findByCarId(int carId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.car.id = :carId AND r.status = 'confirmed' " +
            "AND (:startDate <= r.endDate AND :endDate >= r.startDate)")
    int countConflictingReservations(int carId, LocalDate startDate, LocalDate endDate);


}