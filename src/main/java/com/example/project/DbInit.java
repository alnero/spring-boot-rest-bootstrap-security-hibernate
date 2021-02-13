package com.example.project;

import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbInit {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DbInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void postConstruct() {
        // add authorities to DB
        for (Role.AvailableRoles role : Role.AvailableRoles.values()) {
            roleService.add(new Role(role.name()));
        }

        // add admin to DB
        User admin = new User(
                "admin",
                "admin",
                (byte) 1,
                "admin@mail.org");
        admin.setPassword("admin");
        admin.setRoles(roleService.getByName(Role.AvailableRoles.ADMIN.name()));
        userService.add(admin);

        // add user to DB
        User user = new User(
                "user",
                "user",
                (byte) 2,
                "user@mail.org");
        user.setPassword("user");
        user.setRoles(roleService.getByName(Role.AvailableRoles.USER.name()));
        userService.add(user);
    }
}
