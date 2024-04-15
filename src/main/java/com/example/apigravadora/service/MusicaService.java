package com.example.apigravadora.service;

import com.example.apigravadora.model.Musica;
import com.example.apigravadora.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicaService {

    @Autowired
    private MusicaRepository musicaRepository;

    public Musica createMusica(Musica musica) {

        try {
            musicaRepository.save(musica);
            return musica;
        }  catch (Exception e) {
            throw new RuntimeException("Não foi possível criar música.", e);
        }
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