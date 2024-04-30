package com.example.apigravadora.Dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlbumRequestDto {

    @NotNull (message = "ID da Banda destino é obrigatorio")
    private Long bandaID;

    @NotBlank (message = "NOME do Album é obrigatorio")
    private String nomeAlbum;

    @NotBlank (message = "RESUMO do Album é obrigatorio")
    private String resumoAlbum;
}
