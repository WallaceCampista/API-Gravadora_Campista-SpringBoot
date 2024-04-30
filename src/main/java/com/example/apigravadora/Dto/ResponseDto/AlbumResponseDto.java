package com.example.apigravadora.Dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data //Anotação que cria automaticamente o Get e Set do atributo.
public class AlbumResponseDto {
    private Long id;
    private String nomeAlbum;
    private String resumoAlbum;
}
