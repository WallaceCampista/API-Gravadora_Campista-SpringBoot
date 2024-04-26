package com.example.apigravadora.model;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Album_Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "AlbumTable") // Anotação que denomina noma da tabela do BD.
public class Album {

    @Id //Anotação para identificar como PK a coluna da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long albumId;

    @NotNull(message = "Nome do Album é obrigatorio")
    @Column(name = "Nome_Album", unique = true)
    private String nomeAlbum;

    @NotNull(message = "Resumo do Album é obrigatorio")
    @Column(name = "Resumo_Album")
    private String resumoAlbum;

    @Column(name = "Duracao_Total")
    private double duracaoTotal;

    @Column(name = "Media_Album")
    private double media;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @ManyToOne(fetch = FetchType.LAZY) //Criando relação de muitos para um
    @JoinColumn(name = "FK_Banda_id", referencedColumnName = "bandaId", nullable = false)
    private Banda banda;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY) //Criando relação de um para muitos
    private List<Musica> musicas;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @OneToMany(mappedBy = "albumID", fetch = FetchType.LAZY) //Criando relação de um para muitos
    private List<Avaliacao_Album_Table> avaliacoes;


    //CONSTRUTOR
    public Album() {
    }

    // Método para calcular a média das avaliações
    @Transient
    public double calcularMedia() {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }

        int total = 0;
        for (Avaliacao_Album_Table avaliacao : avaliacoes) {
            total += (int) avaliacao.getNota();
        }

        return (double) total / avaliacoes.size();
    }

    // Método para calcular a duração total das músicas do álbum
    public double calcularDuracaoTotal() {
        if (musicas == null || musicas.isEmpty()) {
            duracaoTotal = 0.0;
            return 0;
        }

        double duracaoTotalCalculada = 0.0;
        for (Musica musica : musicas) {
            duracaoTotalCalculada += musica.getDuracaoMusica();
        }

        duracaoTotal = duracaoTotalCalculada;
        return duracaoTotalCalculada;
    }
}