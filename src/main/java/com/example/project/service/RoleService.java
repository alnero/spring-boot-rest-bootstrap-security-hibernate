package com.example.project.service;

import com.example.project.model.Role;

import java.util.Optional;

public interface RoleService {
    Iterable<Role> listRoles();
    Optional<Role> getById(long id);
    Role getByName(String name);
    void add(Role role);
    void edit(Role role);
    void delete(Role role);
}
