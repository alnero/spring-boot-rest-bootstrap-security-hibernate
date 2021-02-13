package com.example.project.service;

import com.example.project.model.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getById(long id);
    void add(User user);
    void edit(User user);
    void delete(User user);
}
