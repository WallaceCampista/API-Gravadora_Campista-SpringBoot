package com.example.apigravadora.services;

import com.example.apigravadora.model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicaDuracaoTotalService {

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private AlbumService albumService;

    public void atualizarDuracaoTotalAlbum() {
        List<Album> albuns = albumService.getAllAlbuns();
        for (Album album : albuns) {
            album.calcularDuracaoTotal();
            albumService.updateAlbum(album);
        }
    }
}