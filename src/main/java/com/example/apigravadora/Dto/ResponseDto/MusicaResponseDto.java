package com.example.apigravadora.Dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data //Anotação que cria automaticamente o Get e Set do atributo.
public class MusicaResponseDto {
    private Long id;
    private String nomeMusica;
    private String descricaoMusica;
    private double duracaoMusica;
}
