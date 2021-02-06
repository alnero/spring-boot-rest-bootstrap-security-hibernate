package com.example.project.repository;

import com.example.project.model.UserAuthority;
import org.springframework.data.repository.CrudRepository;

public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Long> {
    UserAuthority getUserAuthorityByAuthority(String authority);
}
