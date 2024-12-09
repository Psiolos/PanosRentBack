package com.panos_rental.panos_rental.service;

import com.panos_rental.panos_rental.entity.Reservation;
import com.panos_rental.panos_rental.entity.ReservationStatus;
import com.panos_rental.panos_rental.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> findReservationsByClientId(int clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> findReservationsByCarId(int carId) {
        return reservationRepository.findByCarId(carId);
    }


        public boolean isCarAvailableForDates(int carId, LocalDate startDate, LocalDate endDate) {
            return reservationRepository.countConflictingReservations(carId, startDate, endDate) == 0;
        }

        public Reservation save(Reservation reservation) {
            return reservationRepository.save(reservation);
        }


    public Reservation cancelReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation != null && reservation.getStatus() == ReservationStatus.confirmed) {
            reservation.setStatus(ReservationStatus.canceled);
            reservationRepository.save(reservation);
        }
        return reservation;
    }

}
