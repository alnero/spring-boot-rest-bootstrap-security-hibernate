package com.example.project.repository;

import com.example.project.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User getUserByEmail(String email);
}
