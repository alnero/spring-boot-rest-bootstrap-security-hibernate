package com.example.project.repository;

import com.example.project.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Set<Role> getRoleByName(String name);
}
