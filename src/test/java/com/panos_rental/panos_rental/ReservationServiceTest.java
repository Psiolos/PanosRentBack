package com.panos_rental.panos_rental;

import com.panos_rental.panos_rental.entity.*;
import com.panos_rental.panos_rental.repository.ReservationRepository;
import com.panos_rental.panos_rental.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    public ReservationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservations() {
        // Setup
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // Act
        List<Reservation> reservations = reservationService.getAllReservations();

        // Assert
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testSaveReservation() {
        // Setup
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setTotalPrice(new BigDecimal("100.00"));

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Act
        Reservation savedReservation = reservationService.saveReservation(reservation);

        // Assert
        assertNotNull(savedReservation);
        assertEquals(1, savedReservation.getId());
        assertEquals(new BigDecimal("100.00"), savedReservation.getTotalPrice());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testFindReservationsByClientId() {
        // Setup
        Reservation reservation = new Reservation();
        reservation.setId(1);

        when(reservationRepository.findByClientId(1)).thenReturn(Arrays.asList(reservation));

        // Act
        List<Reservation> reservations = reservationService.findReservationsByClientId(1);

        // Assert
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findByClientId(1);
    }

    @Test
    public void testFindReservationsByCarId() {
        // Setup
        Reservation reservation = new Reservation();
        reservation.setId(2);

        when(reservationRepository.findByCarId(2)).thenReturn(Arrays.asList(reservation));

        // Act
        List<Reservation> reservations = reservationService.findReservationsByCarId(2);

        // Assert
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findByCarId(2);
    }

    @Test
    public void testIsCarAvailableForDates() {
        // Setup
        int carId = 1;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        when(reservationRepository.countConflictingReservations(carId, startDate, endDate)).thenReturn(0);

        // Act
        boolean isAvailable = reservationService.isCarAvailableForDates(carId, startDate, endDate);

        // Assert
        assertTrue(isAvailable);
        verify(reservationRepository, times(1)).countConflictingReservations(carId, startDate, endDate);
    }

    @Test
    public void testCancelReservation() {
        // Setup
        int reservationId = 1;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setStatus(ReservationStatus.confirmed);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation canceledReservation = reservationService.cancelReservation(reservationId);

        // Assert
        assertNotNull(canceledReservation);
        assertEquals(ReservationStatus.canceled, canceledReservation.getStatus());
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testCancelReservation_NotFound() {
        // Setup
        int reservationId = 1;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Reservation result = reservationService.cancelReservation(reservationId);

        // Assert
        assertNull(result);
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(0)).save(any());
    }
}
