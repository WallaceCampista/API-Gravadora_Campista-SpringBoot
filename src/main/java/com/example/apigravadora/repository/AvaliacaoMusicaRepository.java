package com.example.apigravadora.repository;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Musica_Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoMusicaRepository extends JpaRepository<Avaliacao_Musica_Table, Long> {
}