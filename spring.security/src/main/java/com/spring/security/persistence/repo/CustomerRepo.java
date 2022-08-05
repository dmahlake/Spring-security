package com.spring.security.persistence.repo;

import com.spring.security.persistence.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmailAddress(String emailAddress);
    Optional<Customer> findByUsernameOrEmailAddress(String username, String emailAddress);
    Optional<Customer> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmailAddress(String emailAddress);
}
