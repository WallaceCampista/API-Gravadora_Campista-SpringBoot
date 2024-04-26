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

        try {
            Banda bandas;

            bandas = this.bandaService.createBanda(bandaRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bandas.getBandaId()).toUri();

            // Criar o objeto de retorno com os dados da banda criada
            BandaDto bandaDto = new BandaDto();
            bandaDto.setId(bandas.getBandaId());
            bandaDto.setNomeBanda(bandas.getNomeBanda());
            bandaDto.setResumoBanda(bandas.getResumoBanda());

            System.out.println("\t##### Banda/Artista " + bandas.getNomeBanda() + " criada! #####");
            System.out.println();

            return ResponseEntity.created(uri).body("Banda/Artista criada com sucesso!\n\n{\n    \"id\": " +
                    bandaDto.getId() + ",\n    \"nomeBanda\": \"" + bandaDto.getNomeBanda() +
                    "\",\n    \"resumoBanda\": \"" + bandaDto.getResumoBanda() + "\"\n}");

        } catch (RuntimeException exception) {
            throw new RuntimeException("Nome e resumo da banda são obrigatórios !!!");
        }
    }
    @GetMapping("/listarnomesbandas")
    public ResponseEntity<String> listarnomesbandas() {
        try {
            List<Banda> bandas = this.bandaService.getAllBanda();


            List<String> nomesBandas = bandas.stream()
                    .map(Banda::getNomeBanda)
                    .toList();

            System.out.println();
            System.out.println("\t##### Listando nomes das Bandas/Artistas! #####"); //retorno no terminal caso de certo.
            System.out.println();

            String mensagem = "Bandas cadastradas:\n\n" + nomesBandas;
            return ResponseEntity.ok().body(mensagem); //retorno de requisição caso de certo.

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar bandas, por favor, tente novamente ", e);
        }
    }
    @GetMapping("/dadoscompletosbanda")
    public ResponseEntity<List<Banda>> dadoscompletosbanda() {
        try {
            List<Banda> bandas = this.bandaService.getAllBanda();

            System.out.println();
            System.out.println("\t##### Listando dados completos das Bandas/Artistas! #####"); //retorno no terminal caso de certo.
            System.out.println();

            return ResponseEntity.ok().body(bandas); //retorno de requisição caso de certo.

        } catch (Exception e) {
                throw new RuntimeException("Não foi possível listar dados completos, por favor, tente novamente ", e);
        }
    }
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

            System.out.println("Banda/Artista avaliada com sucesso!"); //Exibe no terminal
            String mensagem = "Nota: " + nota + ", atribuída a banda/artista: " + banda.getNomeBanda() + ", com sucesso!"; //configura msg para exibir no corpo da requisição

            return ResponseEntity.ok().body(mensagem);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível avaliar a banda/artista com id: " + id, e);
        }
    }
}