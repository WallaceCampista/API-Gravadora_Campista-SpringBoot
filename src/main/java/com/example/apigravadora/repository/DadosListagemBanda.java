package com.example.apigravadora.repository;

import com.example.apigravadora.model.Banda;

public record DadosListagemBanda(Long id, String nome) {

    public DadosListagemBanda(Banda banda) {
        this(banda.getBandaId(),banda.getNomeBanda());
    }
}