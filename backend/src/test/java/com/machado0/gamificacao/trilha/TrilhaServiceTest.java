package com.machado0.gamificacao.trilha;

import com.machado0.gamificacao.alternativa.Alternativa;
import com.machado0.gamificacao.alternativa.AlternativaRepository;
import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.aula.AulaRepository;
import com.machado0.gamificacao.curso.Curso;
import com.machado0.gamificacao.curso.CursoRepository;
import com.machado0.gamificacao.modulo.Modulo;
import com.machado0.gamificacao.modulo.ModuloRepository;
import com.machado0.gamificacao.questao.Dificuldade;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.questao.QuestaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrilhaServiceTest {
    @Mock private CursoRepository cursoRepository;
    @Mock private ModuloRepository moduloRepository;
    @Mock private AulaRepository aulaRepository;
    @Mock private QuestaoRepository questaoRepository;
    @Mock private AlternativaRepository alternativaRepository;

    private TrilhaService service;
    private Curso curso;
    private Questao questao;
    private Alternativa alternativa;

    @BeforeEach
    void setUp() {
        service = new TrilhaService(cursoRepository, moduloRepository, aulaRepository,
                questaoRepository, alternativaRepository);
        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Matemática - Ensino Médio");
        curso.setDescricao("Trilha");
        Modulo modulo = new Modulo();
        modulo.setId(2L);
        modulo.setCurso(curso);
        modulo.setNome("Módulo 1");
        Aula aula = new Aula();
        aula.setId(3L);
        aula.setModulo(modulo);
        aula.setNome("Aula 1");
        questao = new Questao();
        questao.setId(4L);
        questao.setAula(aula);
        questao.setCabecalho("2 + 2 = ?");
        questao.setDificuldade(Dificuldade.FACIL);
        questao.setXpRecompensa(10);
        alternativa = new Alternativa();
        alternativa.setId(5L);
        alternativa.setQuestao(questao);
        alternativa.setTexto("4");
    }

    @Test
    void deveMontarTrilhaOrdenadaSemExporGabarito() {
        Modulo modulo = questao.getAula().getModulo();
        Aula aula = questao.getAula();
        when(cursoRepository.findByNome("Matemática - Ensino Médio")).thenReturn(Optional.of(curso));
        when(moduloRepository.findByCursoOrderByOrdemAsc(curso)).thenReturn(List.of(modulo));
        when(aulaRepository.findByModuloOrderByOrdemAsc(modulo)).thenReturn(List.of(aula));
        when(questaoRepository.findByAulaOrderByIdAsc(aula)).thenReturn(List.of(questao));
        when(alternativaRepository.findByQuestaoOrderByIdAsc(questao)).thenReturn(List.of(alternativa));

        TrilhaResponseDTO response = service.buscarMatematica();

        assertEquals(1L, response.id());
        QuestaoResponseDTO questaoResponse = response.modulos().get(0).aulas().get(0).questoes().get(0);
        assertEquals("2 + 2 = ?", questaoResponse.cabecalho());
        assertEquals("4", questaoResponse.alternativas().get(0).texto());
        assertFalse(questaoResponse.toString().contains("correta"));
        assertFalse(questaoResponse.toString().contains("justificativa"));
    }

    @Test
    void deveRetornar404DeDominioQuandoTrilhaNaoExistir() {
        when(cursoRepository.findByNome("Matemática - Ensino Médio")).thenReturn(Optional.empty());

        assertThrows(TrilhaNaoEncontradaException.class, () -> service.buscarMatematica());
        verifyNoInteractions(moduloRepository, aulaRepository, questaoRepository, alternativaRepository);
    }
}
