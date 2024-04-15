package com.example.apigravadora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.W
@Table (name = "UsuarioTable") // Anotação que denomina noma da tabela do BD.
public class Usuario {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @NotBlank
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank
    @Column(name = "password")
    private String password;

    //CONSTRUTORES
    public Usuario() {
    }
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
