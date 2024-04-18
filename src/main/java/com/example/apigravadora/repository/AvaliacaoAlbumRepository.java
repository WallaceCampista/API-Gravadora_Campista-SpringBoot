package com.example.apigravadora.repository;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Album_Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoAlbumRepository extends JpaRepository<Avaliacao_Album_Table, Long> {
}