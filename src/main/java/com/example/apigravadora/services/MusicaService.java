package com.example.apigravadora.services;

import com.example.apigravadora.Dto.RequestDto.MusicaRequestDto;
import com.example.apigravadora.model.Album;
import com.example.apigravadora.model.Musica;
import com.example.apigravadora.repository.AlbumRepository;
import com.example.apigravadora.repository.MusicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicaService {

    @Autowired
    private MusicaRepository musicaRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Transactional
    public Musica createMusica(MusicaRequestDto musicaRequestDto) {


            Musica musicaEntity = new Musica();
            musicaEntity.setNomeMusica(musicaRequestDto.getNomeMusica());
            musicaEntity.setDescricaoMusica(musicaRequestDto.getResumoMusica());
            musicaEntity.setDuracaoMusica(musicaRequestDto.getDuracaoMusica());

            Optional<Album> album = Optional.ofNullable(albumRepository.findById(musicaRequestDto.getAlbumID()).orElseThrow(() -> new RuntimeException("Album náo encontrado")));

            if (album.isPresent()) {
                musicaEntity.setAlbum(album.get());
            }

            musicaRepository.save(musicaEntity);

            return musicaEntity;
    }

    public List<Musica> getAllMusicas() {
        return musicaRepository.findAll();
    }

    public Musica getMusicaById(Long id) {
        return musicaRepository.findById(id).orElse(null);
    }

    public void updateMusica(Musica musica) {
        try {
            musicaRepository.save(musica);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar musica.", e);
        }
    }

    public void deleteMusica(Long id) {
        try {
            musicaRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir musica com id: " + id, e);
        }
    }
}