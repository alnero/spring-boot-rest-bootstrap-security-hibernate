package com.example.project.service;

import com.example.project.model.Role;
import com.example.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> listRoles() {
        List<Role> result = new ArrayList<>();
        roleRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Role getById(long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Role not found with id: " + id));
        return role;
    }

    @Override
    public Set<Role> getByName(String name) {
        return roleRepository.getRoleByName(name);
    }

    @Override
    public void add(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void edit(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
