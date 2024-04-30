package com.example.apigravadora.services;

import com.example.apigravadora.Dto.RequestDto.BandaRequestDto;
import com.example.apigravadora.model.Banda;
import com.example.apigravadora.repository.BandaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BandaService {

    @Autowired
    private BandaRepository bandaRepository;

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public Banda createBanda(BandaRequestDto banda) {

        // Verificar se a banda já existe pelo nome
        if (bandaRepository.existsByNomeBanda(banda.getNomeBanda())) {
            throw new DataIntegrityViolationException("Já existe uma banda com o nome fornecido.");
        }
        try {
            Banda bandaEntity = new Banda();
            bandaEntity.setNomeBanda(banda.getNomeBanda());
            bandaEntity.setResumoBanda(banda.getResumoBanda());

            bandaRepository.save(bandaEntity);

            return bandaEntity;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar a banda.", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public List<Banda> getAllBanda() {
        return bandaRepository.findAll();
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public Banda getBandaById(Long id) {
        return bandaRepository.findById(id).orElse(null);
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public void updateBanda(Banda banda) {
        try {
            bandaRepository.save(banda);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a banda.", e);
        }
    }

    @Transactional //se ocorrer uma exceção durante a execução do método, a transação será revertida
    public void deleteBanda(Long id) {
        try {
            bandaRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a banda com id: " + id, e);
        }
    }
}