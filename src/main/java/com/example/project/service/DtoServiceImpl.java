package com.example.project.service;

import com.example.project.dto.UserDto;
import com.example.project.model.Role;
import com.example.project.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DtoServiceImpl implements DtoService {
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    @Autowired
    public DtoServiceImpl(RoleService roleService) {
        this.roleService = roleService;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        userDto.setRole(user.getAuthorities().iterator().next().getAuthority());
        return userDto;
    }

    @Override
    public User convertToEntity(UserDto userDto) {
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
