package com.example.project.service;

import com.example.project.dto.UserDto;
import com.example.project.model.User;

public interface DtoService {
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);
}
