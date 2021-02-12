package com.example.project.service;

import com.example.project.model.UserAuthority;
import com.example.project.repository.UserAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserAuthorityServiceImpl implements UserAuthorityService {
    private final UserAuthorityRepository userAuthorityRepository;

    @Autowired
    public UserAuthorityServiceImpl(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    public Iterable<UserAuthority> listUserAuthorities() {
        return userAuthorityRepository.findAll();
    }

    @Override
    public Optional<UserAuthority> getById(long id) {
        return userAuthorityRepository.findById(id);
    }

    @Override
    public UserAuthority getUserAuthorityByName(String name) {
        return userAuthorityRepository.getUserAuthorityByAuthority(name);
    }

    @Override
    public void add(UserAuthority userAuthority) {
        userAuthorityRepository.save(userAuthority);
    }

    @Override
    public void edit(UserAuthority userAuthority) {
        userAuthorityRepository.save(userAuthority);
    }

    @Override
    public void delete(UserAuthority userAuthority) {
        userAuthorityRepository.delete(userAuthority);
    }
}
