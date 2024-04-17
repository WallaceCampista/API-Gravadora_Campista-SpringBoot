package com.example.apigravadora.services;

import com.example.apigravadora.Dto.RequestDto.AlbumRequestDto;
import com.example.apigravadora.model.Album;
import com.example.apigravadora.model.Banda;
import com.example.apigravadora.repository.AlbumRepository;
import com.example.apigravadora.repository.BandaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private BandaRepository bandaRepository;

    @Transactional
    public Album createAlbum(AlbumRequestDto album) {

        try {
            Album albumEntity = new Album();
            albumEntity.setNomeAlbum(album.getNomeAlbum());
            albumEntity.setResumoAlbum(album.getResumoAlbum());

            Optional<Banda> banda = bandaRepository.findById(album.getBandaID());

            if (banda.isPresent()) {
                albumEntity.setBanda(banda.get());
            }

            albumRepository.save(albumEntity);

            return albumEntity;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar o album.", e);
        }
    }

    @Transactional
    public List<Album> getAllAlbuns() {
        return albumRepository.findAll();
    }

    @Transactional
    public Album getAlbumById(Long id) {
        return albumRepository.findById(id).orElse(null);
    }

    public void updateAlbum(Album album) {
        try {
            albumRepository.save(album);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar o álbum.", e);
        }
    }

    public void deleteAlbum(Long id) {
        try {
            albumRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o álbum com id: " + id, e);
        }
    }
}