package com.spring.security.persistence.repo;

import com.spring.security.persistence.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);
}
