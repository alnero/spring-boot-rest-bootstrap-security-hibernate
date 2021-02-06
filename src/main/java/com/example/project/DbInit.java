package com.example.project;

import com.example.project.model.User;
import com.example.project.model.UserAuthority;
import com.example.project.service.UserAuthorityService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbInit {
    private UserService userService;
    private UserAuthorityService userAuthorityService;

    @Autowired
    public DbInit(UserService userService, UserAuthorityService userAuthorityService) {
        this.userService = userService;
        this.userAuthorityService = userAuthorityService;
    }

    @PostConstruct
    private void postConstruct() {
        // add authorities to DB
        for (UserAuthority.Role role : UserAuthority.Role.values()) {
            userAuthorityService.add(new UserAuthority(role.name()));
        }

        // add admin to DB
        User admin = new User(
                "admin",
                "admin",
                (byte) 1,
                "admin@mail.org");
        admin.setPassword("admin");
        admin.setUserAuthority(userAuthorityService.getUserAuthorityByName(UserAuthority.Role.ADMIN.name()));
        userService.add(admin);

        // add user to DB
        User user = new User(
                "user",
                "user",
                (byte) 2,
                "user@mail.org");
        user.setPassword("user");
        user.setUserAuthority(userAuthorityService.getUserAuthorityByName(UserAuthority.Role.USER.name()));
        userService.add(user);
    }
}
