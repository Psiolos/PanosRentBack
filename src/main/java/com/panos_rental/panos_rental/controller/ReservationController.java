package com.panos_rental.panos_rental.controller;

import com.panos_rental.panos_rental.DTO.CarDTO;
import com.panos_rental.panos_rental.DTO.ClientReservationRequest;
import com.panos_rental.panos_rental.DTO.ReservationRequest;
import com.panos_rental.panos_rental.entity.Car;
import com.panos_rental.panos_rental.entity.Client;
import com.panos_rental.panos_rental.entity.Reservation;
import com.panos_rental.panos_rental.entity.ReservationStatus;
import com.panos_rental.panos_rental.service.CarService;
import com.panos_rental.panos_rental.service.ClientService;
import com.panos_rental.panos_rental.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CarService carService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/available-cars")
    public ResponseEntity<List<Car>> getAvailableCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Car> availableCars = carService.findAvailableCars(startDate, endDate);
        return ResponseEntity.ok(availableCars);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }


    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest) {


        //elegxos an yparxei o pelatis allios ftiaxnei neo
        Client client = clientService.findByPhoneOrIdCard(reservationRequest.getPhone(), reservationRequest.getIdCard());
        if (client == null) {
            client = new Client();
            client.setFirstName(reservationRequest.getFirstName());
            client.setLastName(reservationRequest.getLastName());
            client.setPhone(reservationRequest.getPhone());
            client.setIdCard(reservationRequest.getIdCard());
            client = clientService.save(client);
        }

        //2plos elegxos gia thn krathsh
        boolean isAvailable = reservationService.isCarAvailableForDates(reservationRequest.getCarId(),
                reservationRequest.getStartDate(),
                reservationRequest.getEndDate());
        if (!isAvailable) {
            return ResponseEntity.badRequest().build();
        }


        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setCar(carService.findById(reservationRequest.getCarId()));
        reservation.setStartDate(reservationRequest.getStartDate());
        reservation.setEndDate(reservationRequest.getEndDate());
        reservation.setStatus(ReservationStatus.confirmed);
        reservation.setTotalPrice(reservationRequest.getTotalPrice());
        reservation.setSuperInsurance(reservationRequest.getSuperInsurance());

        Reservation savedReservation = reservationService.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<ReservationRequest>> getReservationsByCarId(@PathVariable int carId) {
        List<Reservation> reservations = reservationService.findReservationsByCarId(carId);


        List<ReservationRequest> reservationRequests = reservations.stream().map(reservation -> {
            ReservationRequest dto = new ReservationRequest();
            dto.setId(reservation.getId());
            dto.setCarId(reservation.getCar().getId());
            dto.setStartDate(reservation.getStartDate());
            dto.setEndDate(reservation.getEndDate());
            dto.setFirstName(reservation.getClient().getFirstName());
            dto.setLastName(reservation.getClient().getLastName());
            dto.setPhone(reservation.getClient().getPhone());
            dto.setIdCard(reservation.getClient().getIdCard());
            dto.setTotalPrice(reservation.getTotalPrice());
            dto.setSuperInsurance(reservation.isSuperInsurance());
            dto.setStatus(reservation.getStatus());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(reservationRequests);
    }


    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable int reservationId) {
        Reservation canceledReservation = reservationService.cancelReservation(reservationId);
        if (canceledReservation != null) {
            return ResponseEntity.ok(canceledReservation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getClientOrders(@PathVariable int clientId) {
        List<Reservation> reservations = reservationService.findReservationsByClientId(clientId);
        return ResponseEntity.ok(reservations);
    }


    /* @GetMapping("")
    public ResponseEntity<List<ClientReservationRequest>> getReservationsByClient(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String idCard) {


        Client client = clientService.findByPhoneOrIdCard(phone, idCard);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        List<Reservation> reservations = reservationService.findReservationsByClientId(client.getId());


        List<ClientReservationRequest> reservationRequests = reservations.stream().map(reservation -> {
            ClientReservationRequest dto = new ClientReservationRequest();
            dto.setId(reservation.getId());
            dto.setCarId(reservation.getCar().getId());
            dto.setStartDate(reservation.getStartDate());
            dto.setEndDate(reservation.getEndDate());
            dto.setFirstName(client.getFirstName());
            dto.setLastName(client.getLastName());
            dto.setPhone(client.getPhone());
            dto.setIdCard(client.getIdCard());
            dto.setTotalPrice(reservation.getTotalPrice());
            dto.setSuperInsurance(reservation.isSuperInsurance());
            dto.setStatus(reservation.getStatus());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(reservationRequests);
    } */

    @GetMapping("/car/details/{id}")
    public ResponseEntity<CarDTO> getCarDetails(@PathVariable Integer id) {
        Car car = carService.findById(id);

        if (car == null) {
            return ResponseEntity.notFound().build();
        }


        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setBrand(car.getBrand());
        carDTO.setModel(car.getModel());
        carDTO.setYear(car.getYear());
        carDTO.setLicensePlate(car.getLicensePlate());
        carDTO.setCategory(car.getCategory());
        carDTO.setIsAvailable(car.getAvailable());
        carDTO.setPrice(car.getPrice());
        carDTO.setPhotoPath(car.getPhotoPath());
        carDTO.setStatus(car.getStatus().toString());
        carDTO.setFuelConsumption(car.getFuelConsumption());
        carDTO.setHorsepower(car.getHorsepower());

        return ResponseEntity.ok(carDTO);
    }


}
