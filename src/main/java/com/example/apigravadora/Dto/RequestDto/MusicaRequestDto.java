package com.example.apigravadora.Dto.RequestDto;

import lombok.Data;

@Data
public class MusicaRequestDto {

    private Long albumID;
    private String nomeMusica;
    private String resumoMusica;
    private double duracaoMusica;
}
