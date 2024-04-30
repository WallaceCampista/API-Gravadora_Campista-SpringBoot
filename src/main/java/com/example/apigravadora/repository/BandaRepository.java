package com.example.apigravadora.repository;

import com.example.apigravadora.model.Banda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BandaRepository extends JpaRepository<Banda, Long> {
//    UserDetails findByBanda(String nomeBanda);
    Page<Banda> findAllByExcluidoFalse(Pageable paginacao);
    boolean existsByNomeBanda(String nomeBanda);
}
