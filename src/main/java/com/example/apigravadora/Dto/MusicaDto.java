package com.example.apigravadora.Dto;

import lombok.Data;

@Data //Anotação que cria automaticamente o Get e Set do atributo.
public class MusicaDto {
    private Long id;
    private String nomeMusica;
    private String resumoMusica;
    private double duracaoMusica;
}
