package com.example.apigravadora.model;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Musica_Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "MusicaTable") // Anotação que denomina noma da tabela do BD.
public class Musica {

    @Id //Anotação para identificar como PK a coluna da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long musicaId;

    @Column(name = "Nome_Musica")
    private String nomeMusica;

    @Column(name = "Resumo_Musica")
    private String descricaoMusica;

    @Column(name = "Media_Musica")
    private double media;

    @Column(name = "Duracao", nullable = false)
    private int duracaoMusica;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @ManyToOne(fetch = FetchType.LAZY) //Criando relação de muitos para um
    @JoinColumn(name = "FK_Album_id", referencedColumnName = "albumId", nullable = false)
    private Album album;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @OneToMany(mappedBy = "musicaID", fetch = FetchType.LAZY) //Criando relação de um para muitos
    private List<Avaliacao_Musica_Table> avaliacoes;


    //CONSTRUTORES
    public Musica() {
    }

    // Método para calcular a média das avaliações
    @Transient
    public double calcularMedia() {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }

        int total = 0;
        for (Avaliacao_Musica_Table avaliacao : avaliacoes) {
            total += (int) avaliacao.getNota();
        }

        return (double) total / avaliacoes.size();
    }
}
