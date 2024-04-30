package com.example.apigravadora.Dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BandaRequestDto {

    @NotBlank (message = "NOME da Banda é obrigatorio")
    private String nomeBanda;

    @NotBlank (message = "RESUMO da Banda é obrigatorio")
    private String resumoBanda;
}
