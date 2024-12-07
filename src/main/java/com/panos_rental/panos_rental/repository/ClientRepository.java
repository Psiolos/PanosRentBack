package com.panos_rental.panos_rental.repository;

import com.panos_rental.panos_rental.entity.Account;
import com.panos_rental.panos_rental.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByPhoneOrIdCard(String phone, String idCard);


    Optional<Client> findByPhone(String phone);

    Optional<Client> findByIdCard(String idCard);

}
