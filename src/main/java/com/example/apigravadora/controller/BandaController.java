package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.BandaDto;
import com.example.apigravadora.model.Avaliacao.Avaliacao_Banda_Table;
import com.example.apigravadora.model.Banda;
import com.example.apigravadora.services.AvaliacaoService;
import com.example.apigravadora.services.BandaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bandas")
public class BandaController {

    @Autowired
    private BandaService bandaService;
    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/novo-registro")
    public ResponseEntity<?> create(@Valid @RequestBody Banda bandaRequest) {
        // Verifica se o nome da banda está vazio
        if (bandaRequest.getNomeBanda().isEmpty()) {
            return ResponseEntity.badRequest().body("Nome da BANDA não pode estar vazio!");
        }

        // Verifica se o resumo da banda está vazio
        if (bandaRequest.getResumoBanda().isEmpty()) {
            return ResponseEntity.badRequest().body("RESUMO da banda não pode estar vazio!");
        }

        Banda bandas;

        bandas = this.bandaService.createBanda(bandaRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bandas.getBandaId()).toUri();

        // Criar o objeto de retorno com os dados da banda criada
        BandaDto bandaDto = new BandaDto();
        bandaDto.setId(bandas.getBandaId());
        bandaDto.setNomeBanda(bandas.getNomeBanda());
        bandaDto.setResumoBanda(bandas.getResumoBanda());

        System.out.println();
        System.out.println("\t##### Banda/Artista " + bandas.getNomeBanda() + " criada! #####");
        System.out.println();

        return ResponseEntity.created(uri).body("Banda/Artista criada com sucesso!\n\n{\n    \"id\": " +
                bandaDto.getId() + ",\n    \"nomeBanda\": \"" + bandaDto.getNomeBanda() +
                "\",\n    \"resumoBanda\": \"" + bandaDto.getResumoBanda() + "\"\n}");
    }
    @GetMapping("/listarbandassimples")
    public ResponseEntity<List<BandaDto>> listarbandassimples() {
        try {
            List<Banda> bandas = bandaService.getAllBanda();

            List<BandaDto> bandasDTO = bandas.stream()
                    .map(banda -> new BandaDto(banda.getBandaId(), banda.getNomeBanda(), banda.getResumoBanda()))
                    .toList();

            System.out.println("\t##### Listando dados simples das Bandas/Artistas! #####");

            return ResponseEntity.ok(bandasDTO);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar bandas, por favor, tente novamente ", e);
        }
    }
    @GetMapping("/listarbandascompleto")
    public ResponseEntity<List<Banda>> listarbandascompleto() {
        try {
            List<Banda> bandas = this.bandaService.getAllBanda();

            System.out.println();
            System.out.println("\t##### Listando dados completos das Bandas/Artistas! #####"); //retorno no terminal.
            System.out.println();

            return ResponseEntity.ok().body(bandas); //retorno de requisição.

        } catch (Exception e) {
                throw new RuntimeException("Não foi possível listar dados completos, por favor, tente novamente ", e);
        }
    }
//    @GetMapping("/paginacaobandascompleto")
//    public ResponseEntity<List<Banda>> paginacaobandascompleto(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        try {
//            Pageable pageable = (Pageable) PageRequest.of(page, size);
//            Page<Banda> bandaPage = this.bandaService.getAllBanda(pageable);
//
//            System.out.println();
//            System.out.println("\t##### Listando dados completos das Bandas/Artistas! #####"); //retorno no terminal caso de certo.
//            System.out.println();
//
//            return ResponseEntity.ok().body(bandaPage.getContent()); //retorno de requisição caso de certo.
//
//        } catch (Exception e) {
//            throw new RuntimeException("Não foi possível listar dados completos, por favor, tente novamente ", e);
//        }
//    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Banda bandaRequest) {
        try {
            Banda existingBanda = bandaService.getBandaById(id);

            if (existingBanda == null) {
                String mensagem = "ID: " + id + " não foi encontrado nos registros.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de id seja invalido.
            }
            // Atualize os dados da banda existente com os fornecidos no bandaRequest
            existingBanda.setNomeBanda(bandaRequest.getNomeBanda());
            existingBanda.setResumoBanda(bandaRequest.getResumoBanda());

            bandaService.updateBanda(existingBanda);

            System.out.println();
            System.out.println("\t##### Banda/Artista " + existingBanda.getNomeBanda() + " atualizada! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Banda/Artista " + existingBanda.getNomeBanda() + " atualizada com sucesso!";
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a banda com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            Banda banda = bandaService.getBandaById(id);

            if (banda == null) {
                String mensagem = "ID: " + id + " não foi encontrado nos registros.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de id seja invalido.
            }

            bandaService.deleteBanda(id);

            System.out.println();
            System.out.println("\t##### Banda com id " + id + " excluída com sucesso! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Banda/Artista com id " + id + " excluída com sucesso!";
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a banda com id: " + id, e);
        }
    }
    @PostMapping("/{id}/avaliar-banda")
    public ResponseEntity<?> avaliarBanda(@PathVariable("id") Long id, @RequestBody Avaliacao_Banda_Table avaliacaoRequest) {
        try {
            Banda banda = bandaService.getBandaById(id);

            if (banda == null) {
                String mensagem = "Banda/Artista não encontrada com o id: " + id;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
            }

            double nota = avaliacaoRequest.getNota();

            // Verifique se a nota está dentro do intervalo desejado (por exemplo, de 0 a 10)
            if (nota < 0 || nota > 10) {
                return ResponseEntity.badRequest().body("Valor inválido!! Informe entre 0 e 10");
            }

            // Crie uma nova avaliação e associe à banda
            avaliacaoRequest.setBandaID(banda);
            // Salve a avaliação no banco de dados
            avaliacaoService.salvarAvaliacaoBanda(avaliacaoRequest);

            // Recalculando a média de notas da banda e atualizando
            banda.setMedia(banda.calcularMedia());
            bandaService.updateBanda(banda);

            String mensagem = "Nota: " + nota + ", atribuída a banda/artista: " + banda.getNomeBanda() + ", com sucesso!";

            System.out.println(mensagem); //Exibe no terminal
            return ResponseEntity.ok().body(mensagem); //Exibe no corpo da requisição

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível avaliar a banda/artista com id: " + id, e);
        }
    }
}