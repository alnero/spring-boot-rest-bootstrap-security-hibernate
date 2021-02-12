package com.example.project.repository;

import com.example.project.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role getRoleByName(String name);
}
