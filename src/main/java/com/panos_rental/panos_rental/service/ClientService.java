package com.panos_rental.panos_rental.service;

import com.panos_rental.panos_rental.entity.Client;
import com.panos_rental.panos_rental.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findByPhoneOrIdCard(String phone, String idCard) {
        Optional<Client> clientByPhone = clientRepository.findByPhone(phone);
        if (clientByPhone.isPresent()) {
            return clientByPhone.get();
        }
        Optional<Client> clientByIdCard = clientRepository.findByIdCard(idCard);
        return clientByIdCard.orElse(null);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public boolean deleteById(int id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(int id) {
        return clientRepository.findById(id);
    }
}
