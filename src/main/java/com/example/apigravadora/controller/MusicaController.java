package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.MusicaDto;
import com.example.apigravadora.Dto.RequestDto.MusicaRequestDto;
import com.example.apigravadora.model.Musica;
import com.example.apigravadora.services.MusicaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/musica")
public class MusicaController {

    @Autowired
    private MusicaService musicaService;

    private final Logger log = LoggerFactory.getLogger(MusicaController.class);

    @PostMapping("/novo-registro")
    public ResponseEntity<?> create(@RequestBody MusicaRequestDto musicaRequest) {

        try {
            Musica musicas = new Musica();

            musicas = this.musicaService.createMusica(musicaRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(musicas.getMusicaId()).toUri();

            // Criar o objeto de retorno com os dados da banda criada
            MusicaDto musicaDto = new MusicaDto();
            musicaDto.setId(musicas.getMusicaId());
            musicaDto.setNomeMusica(musicas.getNomeMusica());
            musicaDto.setResumoMusica(musicas.getDescricaoMusica());
            musicaDto.setDuracaoMusica(musicas.getDuracaoMusica());

            System.out.println();
            System.out.println("\t##### Musica criada! #####");
            System.out.println();

            return ResponseEntity.created(uri).body("Musica criada com sucesso!\n\n{\n    \"id\": " +
                    musicaDto.getId() + ",\n    \"nomeMusica\": \"" + musicaDto.getNomeMusica() +
                    "\",\n    \"resumoMusica\": \"" + musicaDto.getResumoMusica() +  "\",\n    \"duracaoMusica\": \""
                    + musicaDto.getDuracaoMusica() + "\"\n}");

        } catch (RuntimeException exception) {
//            log.error(exception.getMessage());
            throw new RuntimeException("Náo foi possível salvar musica", exception);
        }
    }
    @GetMapping("/listartodasmusicas")
    public ResponseEntity<List<Musica>> listarTodosMusicas() {

        List<Musica> musicas = this.musicaService.getAllMusicas();

        System.out.println();
        System.out.println("\t##### Listando todas as Musicas! #####");
        System.out.println();

        return ResponseEntity.ok().body(musicas);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Musica musicaRequest) {
        try {
            Musica existingMusica = musicaService.getMusicaById(id);

            if (existingMusica == null) {
                return ResponseEntity.notFound().build();
            }
            // Atualize os dados da musica existente com os fornecidos no musicaRequest
            existingMusica.setNomeMusica(musicaRequest.getNomeMusica());
            existingMusica.setDescricaoMusica(musicaRequest.getDescricaoMusica());
            existingMusica.setDuracaoMusica(musicaRequest.getDuracaoMusica());

            musicaService.updateMusica(existingMusica);

            System.out.println();
            System.out.println("\t##### Musica com id " + id + " excluida com sucesso! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar musica com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            Musica musica = musicaService.getMusicaById(id);

            if (musica == null) {
                return ResponseEntity.notFound().build();
            }

            musicaService.deleteMusica(id);

            System.out.println();
            System.out.println("\t##### Musica com id " + id + " deletada! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir musica com id: " + id, e);
        }
    }
}
