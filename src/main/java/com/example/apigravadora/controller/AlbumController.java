package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.AlbumDto;
import com.example.apigravadora.Dto.RequestDto.AlbumRequestDto;
import com.example.apigravadora.model.Album;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Album_Table;
import com.example.apigravadora.services.AlbumService;
import com.example.apigravadora.services.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
            Album albuns;

            albuns = this.albumService.createAlbum(albumRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(albuns.getAlbumId()).toUri();

            // Criar o objeto de retorno com os dados do álbum criado
            AlbumDto albumDto = new AlbumDto();
            albumDto.setId(albuns.getAlbumId());
            albumDto.setNomeAlbum(albuns.getNomeAlbum());
            albumDto.setResumoAlbum(albuns.getResumoAlbum());

            System.out.println("\t##### Album " + albuns.getNomeAlbum() + " criado! #####");
            System.out.println();

            return ResponseEntity.created(uri).body("Album criado com sucesso!\n\n{\n    \"id\": " +
                    albumDto.getId() + ",\n    \"nomeAlbum\": \"" + albumDto.getNomeAlbum() +
                    "\",\n    \"resumoAlbum\": \"" + albumDto.getResumoAlbum() + "\"\n}");

        } catch (RuntimeException exception) {
            throw new RuntimeException("- Este id é valido para uma banda existente ?" + "\n- Preencheu (id, Nome, Resumo) obrigatórios?\n\n");
        }
    }
    @GetMapping("/listarnomesalbuns")
    public ResponseEntity<String> listarnomesalbuns() {
        try {
            List<Album> albuns = this.albumService.getAllAlbuns();


            List<String> nomesAlbuns = albuns.stream()
                    .map(Album::getNomeAlbum)
                    .toList();

            System.out.println();
            System.out.println("\t##### Listando nomes dos Albuns! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Albuns cadastrados:\n\n" + nomesAlbuns;
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar albuns, por favor, tente novamente ", e);
        }
    }
    @GetMapping("/dadoscompletosbanda")
    public ResponseEntity<List<Album>> dadoscompletosbanda() {
        try {
            List<Album> albuns = this.albumService.getAllAlbuns();

            System.out.println();
            System.out.println("\t##### Listando dados completos dos Albuns! #####"); //retorno no terminal caso de certo.
            System.out.println();

            return ResponseEntity.ok().body(albuns); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar dados completos, por favor, tente novamente ", e);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Album albumRequest) {
        try {
            Album existingAlbum = albumService.getAlbumById(id);

            if (existingAlbum == null) {
                String mensagem = "ID: " + id + " não foi encontrado nos registros.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de id seja invalido.
            }
            // Atualize os dados do album existente com os dados fornecidos no albumRequest
            existingAlbum.setNomeAlbum(albumRequest.getNomeAlbum());
            existingAlbum.setResumoAlbum(albumRequest.getResumoAlbum());

            albumService.updateAlbum(existingAlbum);

            System.out.println();
            System.out.println("\t##### Album " + existingAlbum.getNomeAlbum() + " atualizado! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Album " + existingAlbum.getNomeAlbum() + " atualizado com sucesso!";
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar o album com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            Album album = albumService.getAlbumById(id);

            if (album == null) {
                String mensagem = "ID: " + id + " não foi encontrado nos registros.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de id seja invalido.
            }

            albumService.deleteAlbum(id);

            System.out.println();
            System.out.println("\t##### Album com id " + id + " excluido com sucesso! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Album com id " + id + " excluído com sucesso!";
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o álbum com id: " + id, e);
        }
    }
    @PostMapping("/{id}/avaliar-album")
    public ResponseEntity<?> avaliarAlbum(@PathVariable("id") Long id, @RequestBody Avaliacao_Album_Table avaliacaoRequest) {
        try {
            Album album = albumService.getAlbumById(id);

            if (album == null) {
                String mensagem = "Album não encontrado com o id: " + id;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
            }

            double nota = avaliacaoRequest.getNota();

            // Verifique se a nota está dentro do intervalo desejado (por exemplo, de 0 a 10)
            if (nota < 0 || nota > 10) {
                return ResponseEntity.badRequest().body("Valor inválido!! Informe entre 0 e 10");
            }

            // Crie uma nova avaliação e associe à album
            avaliacaoRequest.setAlbumID(album);
            // Salve a avaliação no banco de dados
            avaliacaoService.salvarAvaliacaoAlbum(avaliacaoRequest);

            // Recalculando a média de notas do album e atualizando
            album.setMedia(album.calcularMedia());
            albumService.updateAlbum(album);

            System.out.println("Album: " + album.getNomeAlbum() + ", avaliado com sucesso!"); //Exibe no terminal
            String mensagem = "Nota: " + nota + ", atribuída ao album: " + album.getNomeAlbum() + ", com sucesso!"; //configura msg para exibir no corpo da requisição

            return ResponseEntity.ok().body(mensagem);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível avaliar o album com id: " + id, e);
        }
    }
}