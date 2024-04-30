package com.example.apigravadora.services;

import com.example.apigravadora.Dto.RequestDto.MusicaRequestDto;
import com.example.apigravadora.model.Album;
import com.example.apigravadora.model.Musica;
import com.example.apigravadora.repository.AlbumRepository;
import com.example.apigravadora.repository.MusicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MusicaService {

    @Autowired
    private MusicaRepository musicaRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private MusicaDuracaoTotalService musicaDuracaoTotalService;

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public Musica createMusica(MusicaRequestDto musica) {

        // Verificar se a banda já existe pelo nome
        if (musicaRepository.existsByNomeMusica(musica.getNomeMusica())) {
            throw new DataIntegrityViolationException("Já existe uma musica com o nome fornecido.");
        }
        try {
            Musica musicaEntity = new Musica();
            musicaEntity.setNomeMusica(musica.getNomeMusica());
            musicaEntity.setDescricaoMusica(musica.getDescricaoMusica());
            musicaEntity.setDuracaoMusica(musica.getDuracaoMusica());

            Optional<Album> album = Optional.ofNullable(albumRepository.findById(musica.getAlbumID()).orElseThrow(() -> new RuntimeException("Album náo encontrado")));

            if (album.isPresent()) {
                musicaEntity.setAlbum(album.get());
            }
            //Atualizando duração total do album.
            musicaDuracaoTotalService.atualizarDuracaoTotalAlbum();
            //Adicionando musica no banco.
            musicaRepository.save(musicaEntity);

            return musicaEntity;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar a banda.", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public List<Musica> getAllMusicas() {
        try{
            List<Musica> musicas = musicaRepository.findAll();
            return musicas;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar musicas, por favor, tente novamente ", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public Musica getMusicaById(Long id) {
        return musicaRepository.findById(id).orElse(null);
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public void updateMusica(Musica musica) {
        try {
            //Atualizando musica no banco.
            musicaRepository.save(musica);
            //Atualizando duração total do album.
            musicaDuracaoTotalService.atualizarDuracaoTotalAlbum();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar musica.", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public void deleteMusica(Long id) {

        try {
            //Removendo musica no banco.
            musicaRepository.deleteById(id);
            //Atualizando duração total do album.
            musicaDuracaoTotalService.atualizarDuracaoTotalAlbum();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir musica com id: " + id, e);
        }
    }
}