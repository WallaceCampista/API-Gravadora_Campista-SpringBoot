package com.example.apigravadora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity  //Anotação que indica que classe é uma entidade.
@Getter @Setter //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "MusicaTable") // Anotação que denomina noma da tabela do BD.
public class Musica {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @ManyToOne //Criando relação de muitos para um
    @JoinColumn(name = "FK_Banda_id")
    private Banda bandaID;

    @ManyToOne //Criando relação de muitos para um
    @JoinColumn(name = "FK_Album_id")
    private Album albumID;

    @NotBlank
    @Column(name = "Nome_Musica")
    private String nomeMusica;

    @Column(name = "Resumo_Musica")
    private String resumoMusica;

    @Column(name = "Media_Musica")
    private double media;

    @NotBlank
    @Column(name = "Duracao")
    private double duracao;

    //CONSTRUTORES
    public Musica() {
    }
    public Musica(String nomeMusica, String resumoMusica) {
        this.nomeMusica = nomeMusica;
        this.resumoMusica = resumoMusica;
    }
}
