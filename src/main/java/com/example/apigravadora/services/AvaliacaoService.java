package com.example.apigravadora.services;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Album_Table;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Banda_Table;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Musica_Table;
import com.example.apigravadora.repository.AvaliacaoAlbumRepository;
import com.example.apigravadora.repository.AvaliacaoBandaRepository;
import com.example.apigravadora.repository.AvaliacaoMusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    @Autowired
    private static AvaliacaoAlbumRepository avaliacaoAlbumRepository;

    @Autowired
    private AvaliacaoBandaRepository avaliacaoBandaRepository;

    @Autowired
    private AvaliacaoMusicaRepository avaliacaoMusicaRepository;

    public Avaliacao_Album_Table salvarAvaliacaoAlbum(Avaliacao_Album_Table avaliacao) {
        return salvarAvaliacao(avaliacao, avaliacaoAlbumRepository);
    }

    public Avaliacao_Banda_Table salvarAvaliacaoBanda(Avaliacao_Banda_Table avaliacao) {
        return salvarAvaliacao(avaliacao, avaliacaoBandaRepository);
    }

    public Avaliacao_Musica_Table salvarAvaliacaoMusica(Avaliacao_Musica_Table avaliacao) {
        return salvarAvaliacao(avaliacao, avaliacaoMusicaRepository);
    }

    private static <T> T salvarAvaliacao(T avaliacao, JpaRepository<T, Long> repository) {
        try {
            return repository.save(avaliacao);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar a avaliação.", e);
        }
    }
}
