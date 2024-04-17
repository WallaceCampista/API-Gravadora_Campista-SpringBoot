package com.example.apigravadora.services;

import com.example.apigravadora.model.Banda;
import com.example.apigravadora.repository.BandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandaService {

    @Autowired
    private BandaRepository bandaRepository;

    public Banda createBanda(Banda banda) {

        try {
            bandaRepository.save(banda);
            System.out.println();
            return banda;
        }  catch (Exception e) {
            throw new RuntimeException("Não foi possível criar a banda.", e);
        }
    }

    public List<Banda> getAllBanda() {
        return bandaRepository.findAll();
    }

    public Banda getBandaById(Long id) {
        return bandaRepository.findById(id).orElse(null);
    }

    public void updateBanda(Banda banda) {
        try {
            bandaRepository.save(banda);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a banda.", e);
        }
    }

    public void deleteBanda(Long id) {
        try {
            bandaRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a banda com id: " + id, e);
        }
    }
}