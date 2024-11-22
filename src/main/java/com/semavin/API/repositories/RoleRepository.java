package com.semavin.API.repositories;

import com.semavin.API.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameIgnoreCase(String roleName);
}
