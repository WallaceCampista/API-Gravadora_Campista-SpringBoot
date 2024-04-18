package com.example.apigravadora.Dto.RequestDto;

import lombok.Data;

@Data
public class MusicaRequestDto {

    private Long albumID;
    private String nomeMusica;
    private String descricaoMusica;
    private int duracaoMusica;
}
