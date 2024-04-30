package com.example.apigravadora.Dto.UserDto;

import com.example.apigravadora.model.User.UserRole;
public record RegisterDTO(String login, String password, UserRole role) {
}