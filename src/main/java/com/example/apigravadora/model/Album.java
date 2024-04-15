package com.example.apigravadora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "AlbumTable") // Anotação que denomina noma da tabela do BD.
public class Album {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long albumId;

    @NotBlank
    @Column(name = "Nome_Album", unique = true)
    private String nomeAlbum;

    @NotBlank
    @Column(name = "Resumo_Album")
    private String resumoAlbum;

    @Column(name = "Duracao_Total")
    private double duracaoTotal;

    @Column(name = "Media_Album")
    private double media;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY) //Criando relação de um para muitos
    private List<Musica> musicas;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @ManyToOne(fetch = FetchType.LAZY) //Criando relação de muitos para um
    @JoinColumn(name = "FK_Banda_id", referencedColumnName = "bandaId", nullable = false)
    private Banda banda;

    //CONSTRUTOR

    public Album() {
    }

    public Album(Long albumId, String nomeAlbum, String resumoAlbum) {
        this.albumId = albumId;
        this.nomeAlbum = nomeAlbum;
        this.resumoAlbum = resumoAlbum;
    }
}
