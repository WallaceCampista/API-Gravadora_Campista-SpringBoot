package com.example.apigravadora.model.Avaliacao;

import com.example.apigravadora.model.Musica;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity  //Anotação que indica que classe é uma entidade.
@Getter @Setter //Anotação que cria automaticamente o Get e Set do atributo.
@Table(name = "Avaliacao_Musica_Table") // Anotação que denomina noma da tabela do BD.
public class Avaliacao_Musica_Table {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_Musica_id")
    private Musica musicaID;

    @Column(name = "Nota_Musica")
    private int nota;
}
