package com.example.apigravadora.repository;

import com.example.apigravadora.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    boolean existsByNomeAlbum(String nomeBanda);
}
