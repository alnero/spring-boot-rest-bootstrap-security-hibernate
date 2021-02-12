package com.example.project.service;

import com.example.project.model.Role;
import com.example.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Iterable<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getById(long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role getByName(String name) {
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
