package com.devspace.auth.repository;

import com.devspace.auth.entity.Role;
import com.devspace.common.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}
