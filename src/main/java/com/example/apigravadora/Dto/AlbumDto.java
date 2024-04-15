package com.example.apigravadora.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Anotação que cria automaticamente o Get e Set do atributo.
public class AlbumDto {
    private Long id;
    private String nomeAlbum;
    private String resumoAlbum;
}
