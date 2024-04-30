package com.example.apigravadora.Dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MusicaRequestDto {

    @NotNull(message = "ID do album destino é obrigatorio")
    private Long albumID;

    @NotBlank(message = "NOME da Musica é obrigatorio")
    private String nomeMusica;

    @NotBlank (message = "DESCRIÇÃO da Musica é obrigatorio")
    private String descricaoMusica;

    @NotNull(message = "DURAÇÃO da Musica é obrigatorio")
    private int duracaoMusica;
}
