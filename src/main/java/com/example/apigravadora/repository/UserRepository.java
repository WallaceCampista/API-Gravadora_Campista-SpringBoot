package com.example.apigravadora.repository;

import com.example.apigravadora.model.User.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);
}
