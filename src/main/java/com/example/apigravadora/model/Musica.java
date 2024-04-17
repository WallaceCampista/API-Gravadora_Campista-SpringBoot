package com.example.apigravadora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "MusicaTable") // Anotação que denomina noma da tabela do BD.
public class Musica {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long musicaId;

    @NotNull
    @Column(name = "Nome_Musica")
    private String nomeMusica;

    @NotNull
    @Column(name = "Resumo_Musica")
    private String descricaoMusica;

    @Column(name = "Media_Musica")
    private double media;

    @Column(name = "Duracao", nullable = false)
    private double duracaoMusica;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @ManyToOne(fetch = FetchType.LAZY) //Criando relação de muitos para um
    @JoinColumn(name = "FK_Album_id", referencedColumnName = "albumId", nullable = false)
    private Album album;

    //CONSTRUTORES
    public Musica() {
    }
    public Musica(String nomeMusica, String resumoMusica) {
        this.nomeMusica = nomeMusica;
        this.descricaoMusica = resumoMusica;
    }
}
