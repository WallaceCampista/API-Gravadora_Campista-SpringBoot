package com.example.apigravadora.repository;

import com.example.apigravadora.model.Banda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandaRepository extends JpaRepository<Banda, Long> {
}
