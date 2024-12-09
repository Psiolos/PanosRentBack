package com.panos_rental.panos_rental.controller;

import com.panos_rental.panos_rental.entity.Client;
import com.panos_rental.panos_rental.entity.Reservation;
import com.panos_rental.panos_rental.service.ClientService;
import com.panos_rental.panos_rental.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create-or-get")
    public ResponseEntity<Client> createOrGetClient(@RequestBody Client client) {
        Client existingClient = clientService.findByPhoneOrIdCard(client.getPhone(), client.getIdCard());
        if (existingClient != null) {
            return ResponseEntity.ok(existingClient);
        }
        return ResponseEntity.ok(clientService.save(client));
    }

    @GetMapping("/{phoneOrId}")
    public ResponseEntity<Client> getClientByPhoneOrId(@PathVariable String phoneOrId) {
        Client client = clientService.findByPhoneOrIdCard(phoneOrId, phoneOrId);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public List<Client> getAllClients() {
        return clientService.findAll();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable int id) {
        return clientService.findById(id)
                .map(client -> ResponseEntity.ok().body(client))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        if (clientService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{phoneOrId}/reservations")
    public ResponseEntity<List<Reservation>> getClientReservations(@PathVariable String phoneOrId) {
        Client client = clientService.findByPhoneOrIdCard(phoneOrId, phoneOrId);
        if (client != null) {
            List<Reservation> reservations = reservationService.findReservationsByClientId(client.getId());
            return ResponseEntity.ok(reservations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //gia dokimi na to vgalw an den to 8elw
    @GetMapping("/{clientId}/reservationss")
    public List<Reservation> getClientReservations(@PathVariable int clientId) {
        return reservationService.findReservationsByClientId(clientId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable int id, @RequestBody Client updatedClient) {
        Client existingClient = clientService.findById(id).orElse(null);

        if (existingClient != null) {
            existingClient.setFirstName(updatedClient.getFirstName());
            existingClient.setLastName(updatedClient.getLastName());
            existingClient.setPhone(updatedClient.getPhone());
            existingClient.setIdCard(updatedClient.getIdCard());

            Client updated = clientService.save(existingClient);
            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.notFound().build();
    }


}
