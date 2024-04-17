package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.user.AuthenticationDTO;
import com.example.apigravadora.Dto.user.LoginResponseDTO;
import com.example.apigravadora.Dto.user.RegisterDTO;
import com.example.apigravadora.model.User.Usuario;
import com.example.apigravadora.infra.security.TokenService;
import com.example.apigravadora.repository.UserRepository;
import com.example.apigravadora.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));}


    @PostMapping("/novo-registro")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Usuario usuarioRequest) {
        try {
            Usuario existeUsuario = usuarioService.getUsuarioById(id);

            if (existeUsuario == null) {
                return ResponseEntity.notFound().build();
            }
            // Atualize os dados dos usuarios existente com os dados fornecidos no usuarioRequest
            existeUsuario.setLogin(usuarioRequest.getLogin());
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