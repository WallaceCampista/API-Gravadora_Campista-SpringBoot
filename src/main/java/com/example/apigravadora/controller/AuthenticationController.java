package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.user.AuthenticationDTO;
import com.example.apigravadora.Dto.user.LoginResponseDTO;
import com.example.apigravadora.Dto.user.RegisterDTO;
import com.example.apigravadora.model.User.Usuario;
import com.example.apigravadora.infra.security.TokenService;
import com.example.apigravadora.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("usuarios")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Usuário ou senha inválidos.");
        }
    }

    @PostMapping("/novo-registro")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO data) {
//        Optional<Usuario> optionalUser = repository.findById(id);
//
//        if (optionalUser.isPresent()) {
//            Usuario user = optionalUser.get();
//            // Atualize os campos relevantes do usuário com base nos dados recebidos em data
//            // Por exemplo:
//            // user.setNome(data.getNome());
//            // user.setEmail(data.getEmail());
//            repository.save(user);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        Optional<Usuario> optionalUser = repository.findById(id);

        if (optionalUser.isPresent()) {
            repository.delete(optionalUser.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}