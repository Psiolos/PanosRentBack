package com.panos_rental.panos_rental.repository;

import com.panos_rental.panos_rental.entity.Account;
import com.panos_rental.panos_rental.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

}
