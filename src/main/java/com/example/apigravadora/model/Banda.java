package com.example.apigravadora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity  //Anotação que indica que classe é uma entidade.
@Getter @Setter //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "BandaTable") // Anotação que denomina noma da tabela do BD.
public class Banda {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long bandaId;

    @NotBlank
    @Column(name = "Nome_Banda", unique = true)
    private String nomeBanda;

    @NotBlank
    @Column(name = "Resumo_Banda")
    private String resumoBanda;

    @Column(name = "Media_Banda")
    private double media;

    @OneToMany(mappedBy = "banda")
    private List<Album> albums;

    //CONSTRUTORES
    public Banda() {
    }

    public Banda(Long bandaId, String nomeBanda, String resumoBanda) {
        this.bandaId = bandaId;
        this.nomeBanda = nomeBanda;
        this.resumoBanda = resumoBanda;
    }
}
