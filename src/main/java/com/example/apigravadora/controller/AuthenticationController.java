package com.example.apigravadora.controller;

import com.example.apigravadora.Dto.user.AuthenticationDTO;
import com.example.apigravadora.Dto.user.LoginResponseDTO;
import com.example.apigravadora.Dto.user.RegisterDTO;
import com.example.apigravadora.model.User.Usuario;
import com.example.apigravadora.infra.security.TokenService;
import com.example.apigravadora.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            // Verifica se o login está vazio
            if (data.login().isEmpty()) {
                return ResponseEntity.badRequest().body("LOGIN não pode estar vazio!");
            }

            // Verifica se a senha está vazia
            if (data.password().isEmpty()) {
                return ResponseEntity.badRequest().body("SENHA não pode estar vazia!");
            }

            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            System.out.println("\t##### Usuário: " + data.login() + " logado #####"); //retorno no terminal caso de certo.
            System.out.println();

            return ResponseEntity.ok(new LoginResponseDTO(token)); //retorno de requisição caso de certo.
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Perfil de usuário não encontrado."); //retorno de requisição caso de errado.
        }
    }
    @PostMapping("/novo-registro")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        // Verifica se o login está vazio
        if (data.login().isEmpty()) {
            return ResponseEntity.badRequest().body("LOGIN não pode estar vazio!");
        }

        // Verifica se a senha está vazia
        if (data.password().isEmpty()) {
            return ResponseEntity.badRequest().body("SENHA não pode estar vazia!");
        }

        // Verifica se o login já existe
        if (this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().body("Login já existe, escolha outro!");
        }

        //Criptografando a senha
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());
        this.repository.save(newUser);

        System.out.println();
        System.out.println("\t##### Usuário: " + data.login() + ", com permissão: " + data.role() + ", criado com sucesso! #####"); //retorno no terminal caso de certo.
        System.out.println();

        return ResponseEntity.ok().body("Usuário criado com sucesso!");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        Optional<Usuario> optionalUser = repository.findById(id);

        if (optionalUser.isPresent()) {
            repository.delete(optionalUser.get());
            String mensagem = "Usuário do id: " + id + ", deletado com sucesso!";
            System.out.println();
            System.out.println(mensagem); //retorno no terminal caso de certo.
            System.out.println();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de certo.
        } else {
            String mensagem = "Usuário não encontrado, revise o id informado.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); //retorno de requisição caso de erro.
        }
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

}
