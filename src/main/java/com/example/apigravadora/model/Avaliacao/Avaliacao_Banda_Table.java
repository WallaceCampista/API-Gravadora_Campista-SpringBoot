package com.example.apigravadora.model.Avaliacao;

import com.example.apigravadora.model.Banda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity  //Anotação que indica que classe é uma entidade.
@Getter @Setter //Anotação que cria automaticamente o Get e Set do atributo.
@Table(name = "Avaliacao_Banda_Table") // Anotação que denomina noma da tabela do BD.
public class Avaliacao_Banda_Table {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_Banda_id")
    private Banda bandaID;

    @Column(name = "Nota_Banda")
    private int nota;
}
