package com.example.apigravadora.Dto.RequestDto;

import lombok.Data;

@Data
public class AlbumRequestDto {

    private Long bandaID;
    private String nomeAlbum;
    private String resumoAlbum;
    private double duracaoTotal;
    private double media;
}
