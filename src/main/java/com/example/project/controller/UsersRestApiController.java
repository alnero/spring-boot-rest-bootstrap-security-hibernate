package com.example.project.controller;

import com.example.project.dto.UserDto;
import com.example.project.model.User;
import com.example.project.model.UserAuthority;
import com.example.project.security.SuccessUserHandler;
import com.example.project.service.UserAuthorityService;
import com.example.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsersRestApiController {
    private final UserService userService;
    private final UserAuthorityService userAuthorityService;
    private final SuccessUserHandler successUserHandler;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersRestApiController(UserService userService,
                                  UserAuthorityService userAuthorityService,
                                  SuccessUserHandler successUserHandler,
                                  ModelMapper modelMapper) {
        this.userService = userService;
        this.userAuthorityService = userAuthorityService;
        this.successUserHandler = successUserHandler;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public List<UserDto> list() {
        List<User> result = new ArrayList<>();
        userService.listUsers().forEach(result::add);
        return result.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        User authenticatedUser = successUserHandler.currentAuthenticatedUser();
        Long authenticatedUserId = authenticatedUser.getId();
        String authenticatedUserAuthority = authenticatedUser.getAuthorities().iterator().next().getAuthority();
        // admin can get any user
        if (UserAuthority.Role.ADMIN.name().equals(authenticatedUserAuthority)) {
            Optional<User> userOptional = userService.getById(id);
            return userOptional
                    .map(user -> new ResponseEntity<>(convertToDto(user), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        // user can only get himself but not other users
        if (id.equals(authenticatedUserId)) {
            return new ResponseEntity<>(convertToDto(authenticatedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> add(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            User user = convertToEntity(userDto);
            userService.add(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users")
    public ResponseEntity<?> edit(@RequestBody UserDto userDto) {
        Long id = userDto.getId();
        if (id != null && userService.getById(id).isPresent()) {
            User user = convertToEntity(userDto);
            userService.edit(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (userService.getById(id).isPresent()) {
            userService.delete(userService.getById(id).get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        userDto.setRole(user.getAuthorities().iterator().next().getAuthority());
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        if (userDto.getAge() == null) {
            userDto.setAge((byte) 0);
        }
        String role = userDto.getRole();
        userDto.setRole(null);
        User user = modelMapper.map(userDto, User.class);
        UserAuthority userAuthority = userAuthorityService.getUserAuthorityByName(role);
        user.setUserAuthority(userAuthority);
        return user;
    }
}
