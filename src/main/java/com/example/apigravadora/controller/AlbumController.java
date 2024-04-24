package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.AlbumDto;
import com.example.apigravadora.model.Album;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Album_Table;
import com.example.apigravadora.services.AlbumService;
import com.example.apigravadora.services.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.apigravadora.Dto.RequestDto.AlbumRequestDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/novo-registro")
    public ResponseEntity<?> create(@Valid @RequestBody AlbumRequestDto albumRequest) {
        try {
            // Salva o álbum no banco de dados
            Album albuns = new Album();

            albuns = this.albumService.createAlbum(albumRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(albuns.getAlbumId()).toUri();

            // Criar o objeto de retorno com os dados do álbum criado
            AlbumDto albumDto = new AlbumDto();
            albumDto.setId(albuns.getAlbumId());
            albumDto.setNomeAlbum(albuns.getNomeAlbum());
            albumDto.setResumoAlbum(albuns.getResumoAlbum());

            System.out.println();
            System.out.println("\t##### Album criado! #####");
            System.out.println();

            return ResponseEntity.created(uri).body("Album criado com sucesso!\n\n{\n    \"id\": " +
                    albumDto.getId() + ",\n    \"nomeAlbum\": \"" + albumDto.getNomeAlbum() +
                    "\",\n    \"resumoAlbum\": \"" + albumDto.getResumoAlbum() + "\"\n}");

        } catch (RuntimeException exception) {
            throw new RuntimeException("- Este id é valido para uma banda existente ?" + "\n- Preencheu (id, Nome, Resumo) obrigatórios?\n\n");
        }
    }
    @GetMapping("/listartodosalbuns")
    public ResponseEntity<List<Album>> listarTodosAlbuns() {

        List<Album> albuns = this.albumService.getAllAlbuns();

        System.out.println();
        System.out.println("\t##### Listando todos os Albuns! #####");
        System.out.println();

        return ResponseEntity.ok().body(albuns);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Album albumRequest) {
        try {
            Album existingAlbum = albumService.getAlbumById(id);

            if (existingAlbum == null) {
                return ResponseEntity.notFound().build();
            }
            // Atualize os dados do album existente com os dados fornecidos no albumRequest
            existingAlbum.setNomeAlbum(albumRequest.getNomeAlbum());
            existingAlbum.setResumoAlbum(albumRequest.getResumoAlbum());

            albumService.updateAlbum(existingAlbum);

            System.out.println();
            System.out.println("\t##### Album com id " + id + "atualizado! #####");
            System.out.println();

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar o album com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            Album album = albumService.getAlbumById(id);

            if (album == null) {
                return ResponseEntity.notFound().build();
            }

            albumService.deleteAlbum(id);

            System.out.println();
            System.out.println("\t##### Album com id " + id + " excluido com sucesso! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o álbum com id: " + id, e);
        }
    }
    @PostMapping("/{id}/avaliar-album")
    public ResponseEntity<?> avaliarAlbum(@PathVariable("id") Long id, @RequestBody Avaliacao_Album_Table avaliacaoRequest) {
        try {
            Album album = albumService.getAlbumById(id);

            if (album == null) {
                return ResponseEntity.notFound().build();
            }

            // Verifique se a nota está dentro do intervalo desejado (por exemplo, de 0 a 10)
            double nota = avaliacaoRequest.getNota();
            if (nota < 0 || nota > 10) {
                return ResponseEntity.badRequest().body("Valor inválido, informe [0 a 10]");
            }

            // Crie uma nova avaliação e associe à banda
            avaliacaoRequest.setAlbumID(album);
            // Salve a avaliação no banco de dados
            avaliacaoService.salvarAvaliacaoAlbum(avaliacaoRequest);

            // Recalculando a média e duração total do album e atualizando os campos
            album.setMedia(album.calcularMedia());

            albumService.updateAlbum(album);

            System.out.println("Album avaliado com sucesso!");

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível avaliar o album com id: " + id, e);
        }
    }
}