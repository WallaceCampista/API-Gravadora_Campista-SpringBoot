package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.MusicaDto;
import com.example.apigravadora.Dto.RequestDto.MusicaRequestDto;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Musica_Table;
import com.example.apigravadora.model.Musica;
import com.example.apigravadora.services.AvaliacaoService;
import com.example.apigravadora.services.MusicaDuracaoTotalService;
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
    @Autowired
    private MusicaDuracaoTotalService musicaDuracaoTotalService;

    @PostMapping("/novo-registro")
    public ResponseEntity<?> create(@Valid @RequestBody MusicaRequestDto musicaRequest) {

        try {
            // Verifica se o nome da musica está vazio
            if (musicaRequest.getNomeMusica().isEmpty()) {
                return ResponseEntity.badRequest().body("NOME da musica não pode estar vazio!");
            }

            // Verifica se o resumo da musica está vazio
            if (musicaRequest.getDescricaoMusica().isEmpty()) {
                return ResponseEntity.badRequest().body("DESCIÇÃO da musica não pode estar vazia!");
            }

            Musica musicas;

            musicas = this.musicaService.createMusica(musicaRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(musicas.getMusicaId()).toUri();

            // Criar o objeto de retorno com os dados da banda criada
            MusicaDto musicaDto = new MusicaDto();
            musicaDto.setId(musicas.getMusicaId());
            musicaDto.setNomeMusica(musicas.getNomeMusica());
            musicaDto.setDescricaoMusica(musicas.getDescricaoMusica());
            musicaDto.setDuracaoMusica(musicas.getDuracaoMusica());

            musicaDuracaoTotalService.atualizarDuracaoTotalGlobalmente();

            System.out.println("\t##### Musica" + musicas.getNomeMusica() + "criada! #####");
            System.out.println();

            return ResponseEntity.created(uri).body("Musica criada com sucesso!\n\n{\n    \"id\": " +
                    musicaDto.getId() + ",\n    \"nomeMusica\": \"" + musicaDto.getNomeMusica() +
                    "\",\n    \"resumoMusica\": \"" + musicaDto.getDescricaoMusica() +  "\",\n    \"duracaoMusica\": \""
                    + musicaDto.getDuracaoMusica() + "\"\n}");

        } catch (RuntimeException exception) {
            throw new RuntimeException("Náo foi possível salvar musica", exception);
        }
    }
    @GetMapping("/listarmusicassimples")
    public ResponseEntity<List<String>> listarmusicassimples() {
        try {
            List<Musica> musicas = this.musicaService.getAllMusicas();


            List<String> nomesMusicas = musicas.stream()
                    .map(Musica::getNomeMusica)
                    .toList();

            System.out.println();
            System.out.println("\t##### Listando nomes das Musicas! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Musicas cadastradas:\n\n" + nomesMusicas;
            return ResponseEntity.ok().body(nomesMusicas); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar musicas, por favor, tente novamente ", e);
        }
    }
    @GetMapping("/listarmusicascompleto")
    public ResponseEntity<List<Musica>> listarmusicascompleto() {
        try {
            List<Musica> musicas = musicaService.getAllMusicas();

            System.out.println();
            System.out.println("\t##### Listando dados completos das Musicas! #####"); //retorno no terminal.
            System.out.println();

            return ResponseEntity.ok().body(musicas); //retorno de requisição.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar musicas, por favor, tente novamente ", e);
        }
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
            musicaDuracaoTotalService.atualizarDuracaoTotalGlobalmente();

            System.out.println();
            System.out.println("\t##### Musica " + existingMusica.getNomeMusica() + " atualizada! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Musica " + existingMusica.getNomeMusica() + " atualizada com sucesso!";
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar musica com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            Musica musica = musicaService.getMusicaById(id);

            if (musica == null) {
                String mensagem = "ID: " + id + " não foi encontrado nos registros.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de id seja invalido.
            }

            musicaService.deleteMusica(id);
            musicaDuracaoTotalService.atualizarDuracaoTotalGlobalmente(); //atualiza atributo ""duracaoTotal

            System.out.println();
            System.out.println("\t##### Musica com id " + id + " deletada! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Musica com id " + id + " excluída com sucesso!";
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir musica com id: " + id, e);
        }
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

            String mensagem = "Nota: " + nota + ", atribuída a musica: " + musica.getNomeMusica() + ", com sucesso!";

            System.out.println(mensagem); //Exibe no terminal
            return ResponseEntity.ok().body(mensagem); //Exibe no corpo da requisição

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível avaliar a musica com id: " + id, e);
        }
    }
}