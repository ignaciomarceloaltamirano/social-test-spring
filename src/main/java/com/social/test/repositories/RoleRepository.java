package com.social.test.repositories;

import com.social.test.entities.Role;
import com.social.test.enumerations.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);

    boolean existsByName(ERole name);
}
