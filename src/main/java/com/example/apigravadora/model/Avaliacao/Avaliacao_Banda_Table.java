package com.example.apigravadora.model.Avaliacao;

import com.example.apigravadora.model.Banda;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@Table(name = "Avaliacao_Banda_Table") // Anotação que denomina noma da tabela do BD.
public class Avaliacao_Banda_Table {

    @Id //Anotação para identificar como PK a coluna da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @JsonIgnore // Ignora a serialização desse campo pelo Jackson
    @ManyToOne
    @JoinColumn(name = "FK_Banda_id")
    private Banda bandaID;

    @Column(name = "Nota_Banda", nullable = false)
    private double nota;
}
