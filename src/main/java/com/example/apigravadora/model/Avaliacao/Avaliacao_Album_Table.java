package com.example.apigravadora.model.Avaliacao;

import com.example.apigravadora.model.Album;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity  //Anotação que indica que classe é uma entidade.
@Getter @Setter //Anotação que cria automaticamente o Get e Set do atributo.
@Table(name = "Avaliacao_Album_Table") // Anotação que denomina noma da tabela do BD.
public class Avaliacao_Album_Table {

    @Id //Anotação para gerar um id para tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_Album_id")
    private Album albumID;

    @Column(name = "Nota_Album")
    private int nota;
}
