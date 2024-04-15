package com.example.apigravadora.service;

import com.example.apigravadora.model.Usuario;
import com.example.apigravadora.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario createUsuario(Usuario usuario) {

        try {
            usuarioRepository.save(usuario);
            return usuario;
        }  catch (Exception e) {
            throw new RuntimeException("Não foi possível criar usuario.", e);
        }
    }

    public List<Usuario> getAllUsuario() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void updateUsuario(Usuario usuario) {
        try {
            usuarioRepository.save(usuario);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a usuario.", e);
        }
    }

    public void deleteUsuario(Long id) {
        try {
            usuarioRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir usuario com id: " + id, e);
        }
    }
}