package com.example.apigravadora.Test;

import com.example.apigravadora.model.Avaliacao.Avaliacao_Banda_Table;
import com.example.apigravadora.model.Banda;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestBanda {

    private Banda banda;

    @Before
    public void setUp() {
        banda = new Banda();
    }

    @Test
    public void testCalcularMediaSemAvaliacoes() {
        double media = banda.calcularMedia();
        assertEquals(0.0, media, 0.001);
    }

    @Test
    public void testCalcularMediaComAvaliacoes() {
        // Criando mocks das avaliações
        Avaliacao_Banda_Table avaliacao1 = mock(Avaliacao_Banda_Table.class);
        Avaliacao_Banda_Table avaliacao2 = mock(Avaliacao_Banda_Table.class);

        // Definindo o comportamento dos mocks
        when(avaliacao1.getNota()).thenReturn(Double.valueOf(3));
        when(avaliacao2.getNota()).thenReturn(Double.valueOf(4));

        // Criando lista de avaliações e adicionando os mocks
        List<Avaliacao_Banda_Table> avaliacoes = new ArrayList<>();
        avaliacoes.add(avaliacao1);
        avaliacoes.add(avaliacao2);

        // Configurando a lista de avaliações na banda
        banda.setAvaliacoes(avaliacoes);

        // Calculando a média
        double media = banda.calcularMedia();

        // Verificando se a média está correta
        assertEquals(3.5, media, 0.001);
    }
}