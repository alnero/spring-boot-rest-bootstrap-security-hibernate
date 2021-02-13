package com.example.project.controller;

import com.example.project.dto.UserDto;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsersRestApiController {
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersRestApiController(UserService userService,
                                  RoleService roleService,
                                  ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> list() {
        List<UserDto> userDtoList = userService.listUsers().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Long authenticatedUserId = authenticatedUser.getId();
        String authenticatedUserRole = authenticatedUser.getAuthorities().iterator().next().getAuthority();
        // admin can get any user
        if (Role.AvailableRoles.ADMIN.name().equals(authenticatedUserRole)) {
            User user = userService.getById(id);
            return new ResponseEntity<>(convertToDto(user), HttpStatus.OK);
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
        if (id != null && userService.getById(id) != null) {
            User user = convertToEntity(userDto);
            userService.edit(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (userService.getById(id) != null) {
            userService.delete(userService.getById(id));
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
        String roleFromDto = userDto.getRole();
        userDto.setRole(null);
        User user = modelMapper.map(userDto, User.class);
        Set<Role> roles = roleService.getByName(roleFromDto);
        user.setRoles(roles);
        return user;
    }
}
