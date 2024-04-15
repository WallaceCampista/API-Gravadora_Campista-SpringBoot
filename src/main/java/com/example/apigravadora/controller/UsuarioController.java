package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.UsuarioDto;
import com.example.apigravadora.model.Usuario;
import com.example.apigravadora.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/novo-registro")
    public ResponseEntity<UsuarioDto> create(@RequestBody Usuario usuarioRequest) {

        try {
            Usuario usuario = new Usuario();

            usuario = this.usuarioService.createUsuario(usuarioRequest);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();

            // Criar o objeto de retorno com os dados da banda criada
            UsuarioDto usuarioDto = new UsuarioDto();
            usuarioDto.setId(usuario.getId());
            usuarioDto.setUsername(usuario.getUsername());

            System.out.println();
            System.out.println("\t##### Usuario criado! #####");
            System.out.println();

            return ResponseEntity.created(uri).body(usuarioDto);

        } catch (RuntimeException exception) {

            throw new RuntimeException("Náo foi possível salvar usuario", exception);
        }
    }
    @GetMapping("/listartodosusuario")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {

        List<Usuario> usuario = this.usuarioService.getAllUsuario();

        System.out.println();
        System.out.println("\t##### Listando todos os Usuários! #####");
        System.out.println();

        return ResponseEntity.ok().body(usuario);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Usuario usuarioRequest) {
        try {
            Usuario existeUsuario = usuarioService.getUsuarioById(id);

            if (existeUsuario == null) {
                return ResponseEntity.notFound().build();
            }
            // Atualize os dados dos usuarios existente com os dados fornecidos no usuarioRequest
            existeUsuario.setUsername(usuarioRequest.getUsername());
            existeUsuario.setPassword(usuarioRequest.getPassword());

            usuarioService.updateUsuario(existeUsuario);

            System.out.println();
            System.out.println("\t##### Usuario com id: " + id + "atualizado! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar o usuário com id: " + id, e);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            Usuario usuario = usuarioService.getUsuarioById(id);

            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }

            usuarioService.deleteUsuario(id);

            System.out.println();
            System.out.println("\t##### Usuario com id " + id + " excluido com sucesso! #####");
            System.out.println();

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o usuário com id: " + id, e);
        }
    }
}
