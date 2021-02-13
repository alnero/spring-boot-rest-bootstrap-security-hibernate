package com.example.project.service;

import com.example.project.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> listRoles();
    Role getById(long id);
    Set<Role> getByName(String name);
    void add(Role role);
    void edit(Role role);
    void delete(Role role);
}
