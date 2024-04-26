package com.example.apigravadora.Dto;

import lombok.Data;

@Data //Anotação que cria automaticamente o Get e Set do atributo.
public class BandaDto {
    private Long id;
    private String nomeBanda;
    private String resumoBanda;
}
