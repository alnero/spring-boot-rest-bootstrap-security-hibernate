package com.example.project.service;

import com.example.project.model.User;

import java.util.Optional;

public interface UserService {
    Iterable<User> listUsers();
    Optional<User> getById(long id);
    void add(User user);
    void edit(User user);
    void delete(User user);
}
