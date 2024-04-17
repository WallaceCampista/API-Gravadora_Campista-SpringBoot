package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.BandaDto;
import com.example.apigravadora.model.Banda;
import com.example.apigravadora.services.BandaService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/novo-registro")
    public ResponseEntity<?> create(@RequestBody Banda bandaRequest) {

        try {
            Banda bandas = new Banda();

            bandas = this.bandaService.createBanda(bandaRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bandas.getBandaId()).toUri();

            // Criar o objeto de retorno com os dados da banda criada
            BandaDto bandaDto = new BandaDto();
            bandaDto.setId(bandas.getBandaId());
            bandaDto.setNomeBanda(bandas.getNomeBanda());
            bandaDto.setResumoBanda(bandas.getResumoBanda());

            System.out.println();
            System.out.println("\t##### Banda criada! #####");
            System.out.println();

            return ResponseEntity.created(uri).body("Banda criada com sucesso!\n\n{\n    \"id\": " +
                    bandaDto.getId() + ",\n    \"nomeBanda\": \"" + bandaDto.getNomeBanda() +
                    "\",\n    \"resumoBanda\": \"" + bandaDto.getResumoBanda() + "\"\n}");

        } catch (RuntimeException exception) {

            throw new RuntimeException("Nome e resumo da banda são obrigátorios !!!");
        }
    }
    @GetMapping("/listartodasbandas")
    public ResponseEntity<List<Banda>> listarTodosBandas() {

        List<Banda> bandas = this.bandaService.getAllBanda();

        System.out.println();
        System.out.println("\t##### Listando todas as Bandas! #####");
        System.out.println();

        return ResponseEntity.ok().body(bandas);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Banda bandaRequest) {
        try {
            Banda existingBanda = bandaService.getBandaById(id);

            if (existingBanda == null) {
                return ResponseEntity.notFound().build();
            }
            // Atualize os dados da banda existente com os fornecidos no bandaRequest
            existingBanda.setNomeBanda(bandaRequest.getNomeBanda());
            existingBanda.setResumoBanda(bandaRequest.getResumoBanda());

            bandaService.updateBanda(existingBanda);

            System.out.println();
            System.out.println("\t##### Banda com id " + id + " atualizada! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a banda com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            Banda banda = bandaService.getBandaById(id);

            if (banda == null) {
                return ResponseEntity.notFound().build();
            }

            bandaService.deleteBanda(id);

            System.out.println();
            System.out.println("\t##### Banda com id " + id + " excluida com sucesso! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a banda com id: " + id, e);
        }
    }
}
