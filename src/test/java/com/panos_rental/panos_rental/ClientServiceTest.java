package com.panos_rental.panos_rental;

import com.panos_rental.panos_rental.entity.Client;
import com.panos_rental.panos_rental.repository.ClientRepository;
import com.panos_rental.panos_rental.service.ClientService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    // Test for findByPhoneOrIdCard - phone found
    @Test
    public void testFindByPhoneOrIdCard_phoneFound() {
        Client client = new Client();
        client.setId(1);
        client.setPhone("123456789");
        client.setIdCard("A123456");

        when(clientRepository.findByPhone("123456789")).thenReturn(Optional.of(client));

        Client result = clientService.findByPhoneOrIdCard("123456789", "A123456");

        assertNotNull(result);
        assertEquals("123456789", result.getPhone());
        assertEquals("A123456", result.getIdCard());
    }

    // Test for findByPhoneOrIdCard - ID card found
    @Test
    public void testFindByPhoneOrIdCard_idCardFound() {
        Client client = new Client();
        client.setId(1);
        client.setPhone("987654321");
        client.setIdCard("A123456");

        when(clientRepository.findByPhone("987654321")).thenReturn(Optional.empty());
        when(clientRepository.findByIdCard("A123456")).thenReturn(Optional.of(client));

        Client result = clientService.findByPhoneOrIdCard("987654321", "A123456");

        assertNotNull(result);
        assertEquals("A123456", result.getIdCard());
    }

    // Test for findByPhoneOrIdCard - not found
    @Test
    public void testFindByPhoneOrIdCard_notFound() {
        when(clientRepository.findByPhone("111222333")).thenReturn(Optional.empty());
        when(clientRepository.findByIdCard("B987654")).thenReturn(Optional.empty());

        Client result = clientService.findByPhoneOrIdCard("111222333", "B987654");

        assertNull(result);
    }

    // Test for save method
    @Test
    public void testSave() {
        Client client = new Client();
        client.setId(1);
        client.setPhone("123456789");
        client.setIdCard("A123456");

        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.save(client);

        assertNotNull(result);
        assertEquals("123456789", result.getPhone());
        assertEquals("A123456", result.getIdCard());
    }

    // Test for deleteById - client exists
    @Test
    public void testDeleteById_exists() {
        int clientId = 1;
        when(clientRepository.existsById(clientId)).thenReturn(true);

        boolean result = clientService.deleteById(clientId);

        verify(clientRepository).deleteById(clientId); // Verify that delete was called
        assertEquals(true, result);
    }

    // Test for deleteById - client does not exist
    @Test
    public void testDeleteById_notExists() {
        int clientId = 1;
        when(clientRepository.existsById(clientId)).thenReturn(false);

        boolean result = clientService.deleteById(clientId);

        assertEquals(false, result);
    }

    // Test for findAll method
    @Test
    public void testFindAll() {
        Client client1 = new Client();
        client1.setId(1);
        client1.setPhone("123456789");
        client1.setIdCard("A123456");

        Client client2 = new Client();
        client2.setId(2);
        client2.setPhone("987654321");
        client2.setIdCard("B123456");

        List<Client> clients = List.of(client1, client2);

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // Test for findById method - client found
    @Test
    public void testFindById_found() {
        int clientId = 1;
        Client client = new Client();
        client.setId(clientId);
        client.setPhone("123456789");
        client.setIdCard("A123456");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.findById(clientId);

        assertNotNull(result);

    }

    // Test for findById method - client not found
    @Test
    public void testFindById_notFound() {
        int clientId = 1;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.findById(clientId);

        assertNull(result.orElse(null));
    }
}
