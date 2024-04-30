package com.example.apigravadora.services;

import com.example.apigravadora.Dto.RequestDto.AlbumRequestDto;
import com.example.apigravadora.model.Album;
import com.example.apigravadora.model.Banda;
import com.example.apigravadora.repository.AlbumRepository;
import com.example.apigravadora.repository.BandaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private BandaRepository bandaRepository;

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public Album createAlbum(AlbumRequestDto album) {

        // Verificar se a banda já existe pelo nome
        if (albumRepository.existsByNomeAlbum(album.getNomeAlbum())) {
            throw new DataIntegrityViolationException("Já existe um album com o nome fornecido.");
        }
        try {
            Album albumEntity = new Album();
            albumEntity.setNomeAlbum(album.getNomeAlbum());
            albumEntity.setResumoAlbum(album.getResumoAlbum());

            Optional<Banda> banda = bandaRepository.findById(album.getBandaID());

            banda.ifPresent(albumEntity::setBanda);

            albumRepository.save(albumEntity);

            return albumEntity;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar o album.", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public List<Album> getAllAlbuns() {
        return albumRepository.findAll();
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public Album getAlbumById(Long id) {
        return albumRepository.findById(id).orElse(null);
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public void updateAlbum(Album album) {
        try {
            albumRepository.save(album);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar o álbum.", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public void deleteAlbum(Long id) {
        try {
            albumRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o álbum com id: " + id, e);
        }
    }
}