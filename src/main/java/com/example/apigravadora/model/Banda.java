package com.example.apigravadora.model;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Banda_Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@Table (name = "BandaTable") // Anotação que denomina noma da tabela do BD.
public class Banda {

    @Id //Anotação para identificar como PK a coluna da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long bandaId;

    @NotNull
    @Column(name = "Nome_Banda", unique = true)
    private String nomeBanda;

    @NotNull
    @Column(name = "Resumo_Banda")
    private String resumoBanda;

    @Column(name = "Media_Banda")
    private double media;

    @OneToMany(mappedBy = "banda", fetch = FetchType.LAZY) //Criando relação de um para muitos
    private List<Album> albums;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @OneToMany(mappedBy = "bandaID", fetch = FetchType.LAZY) //Criando relação de um para muitos
    private List<Avaliacao_Banda_Table> avaliacoes;

    //CONSTRUTORES
    public Banda() {
    }

    // Método para calcular a média das avaliações
    @Transient
    public double calcularMedia() {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }

        int total = 0;
        for (Avaliacao_Banda_Table avaliacao : avaliacoes) {
            total += (int) avaliacao.getNota();
        }

        return (double) total / avaliacoes.size();
    }
}