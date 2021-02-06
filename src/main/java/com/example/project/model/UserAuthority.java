package com.example.project.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class UserAuthority implements GrantedAuthority {
    public enum Role {
        USER, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String authority;

    public UserAuthority() {

    }

    public UserAuthority(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
