package com.example.project.service;

import com.example.project.model.UserAuthority;

import java.util.Optional;

public interface UserAuthorityService {
    Iterable<UserAuthority> listUserAuthorities();
    Optional<UserAuthority> getById(long id);
    UserAuthority getUserAuthorityByName(String name);
    void add(UserAuthority userAuthority);
    void edit(UserAuthority userAuthority);
    void delete(UserAuthority userAuthority);
}
