package com.example.apigravadora.repository;

import com.example.apigravadora.model.Banda;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.print.Pageable;

public interface BandaRepository extends JpaRepository<Banda, Long> {
//    UserDetails findByBanda(String nomeBanda);
}
