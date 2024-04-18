package com.example.apigravadora.repository;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Banda_Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoBandaRepository extends JpaRepository<Avaliacao_Banda_Table, Long> {
}