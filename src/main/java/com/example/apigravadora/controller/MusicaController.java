package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.RequestDto.MusicaRequestDto;
import com.example.apigravadora.Dto.ResponseDto.MusicaResponseDto;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Musica_Table;
import com.example.apigravadora.model.Musica;
import com.example.apigravadora.services.AvaliacaoService;
import com.example.apigravadora.services.MusicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private AvaliacaoService avaliacaoService;


    @PostMapping("/novo-registro")
    public ResponseEntity<?> create(@Valid @RequestBody MusicaRequestDto musicaRequest) {

            Musica musicas;

            musicas = this.musicaService.createMusica(musicaRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(musicas.getMusicaId()).toUri();

            // Criar o objeto de retorno com os dados da banda criada
            MusicaResponseDto musicaResponseDto = new MusicaResponseDto();
            musicaResponseDto.setId(musicas.getMusicaId());
            musicaResponseDto.setNomeMusica(musicas.getNomeMusica());
            musicaResponseDto.setDescricaoMusica(musicas.getDescricaoMusica());
            musicaResponseDto.setDuracaoMusica(musicas.getDuracaoMusica());

            return ResponseEntity.created(uri).body(musicaResponseDto);
    }
    @GetMapping("/listarmusicassimples")
    public ResponseEntity<List<MusicaResponseDto>> listarmusicassimples() {
        try {
            List<Musica> musicas = this.musicaService.getAllMusicas();

            List<MusicaResponseDto> musicaResponseDto = musicas.stream()
                    .map(musica -> new MusicaResponseDto(musica.getMusicaId(), musica.getNomeMusica(),musica.getDescricaoMusica(), musica.getDuracaoMusica()))
                    .toList();

            return ResponseEntity.ok().body(musicaResponseDto); //retorno de requisição caso de certo.
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar musicas, por favor, tente novamente ", e);
        }
    }
    @GetMapping("/listarmusicascompleto")
    public ResponseEntity<List<Musica>> listarmusicascompleto() {

            List<Musica> musicas = musicaService.getAllMusicas();

            return ResponseEntity.ok().body(musicas); //retorno de requisição.
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Musica musicaRequest) {
        try {
            Musica existingMusica = musicaService.getMusicaById(id);

            if (existingMusica == null) {
                String mensagem = "ID: " + id + " não foi encontrado nos registros.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de id seja invalido.
            }
            // Atualize os dados da musica existente com os fornecidos no musicaRequest
            existingMusica.setNomeMusica(musicaRequest.getNomeMusica());
            existingMusica.setDescricaoMusica(musicaRequest.getDescricaoMusica());
            existingMusica.setDuracaoMusica(musicaRequest.getDuracaoMusica());

            musicaService.updateMusica(existingMusica);

            return ResponseEntity.ok().body("Musica " + existingMusica.getNomeMusica() + " atualizada com sucesso!"); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a banda com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        musicaService.deleteMusica(id);

        return ResponseEntity.ok().body("Musica com id " + id + " excluída com sucesso!"); //retorno de requisição caso de certo.
}
    @PostMapping("/{id}/avaliar-musica")
    public ResponseEntity<?> avaliarBanda(@PathVariable("id") Long id, @RequestBody Avaliacao_Musica_Table avaliacaoRequest) {
        try {
            Musica musica = musicaService.getMusicaById(id);

            if (musica == null) {
                String mensagem = "Musica não encontrado com o id: " + id;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
            }

            double nota = avaliacaoRequest.getNota();

            // Verifique se a nota está dentro do intervalo desejado (por exemplo, de 0 a 10)
            if (nota < 1 || nota > 10) {
                return ResponseEntity.badRequest().body("Valor inválido!! Informe entre 0 e 10");
            }

            // Crie uma nova avaliação e associe à banda
            avaliacaoRequest.setMusicaID(musica);
            // Salve a avaliação no banco de dados
            avaliacaoService.salvarAvaliacaoMusica(avaliacaoRequest);

            // Recalculando a média de notas da musica e atualizando
            musica.setMedia(musica.calcularMedia());
            musicaService.updateMusica(musica);

            return ResponseEntity.ok().body("Nota: " + nota + ", atribuída a musica: " + musica.getNomeMusica() + ", com sucesso!"); //Exibe no corpo da requisição

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível avaliar a musica com id: " + id, e);
        }
    }
}