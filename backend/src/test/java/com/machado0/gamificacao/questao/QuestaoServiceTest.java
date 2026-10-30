package com.machado0.gamificacao.questao;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class QuestaoServiceTest {

    private QuestaoRepository questaoRepository = Mockito.mock(QuestaoRepository.class);
    private DtoMapper mapper;
    private QuestaoService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class),
                Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new QuestaoService(questaoRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Questao salva = new Questao();
        salva.setId(31L);
        salva.setCabecalho("Quanto é 7 + 5?");

        when(questaoRepository.save(any(Questao.class))).thenReturn(salva);

        QuestaoDto dto = new QuestaoDto();
        dto.setCabecalho("Quanto é 7 + 5?");

        QuestaoDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(31L, out.getId());
        assertEquals("Quanto é 7 + 5?", out.getCabecalho());
    }

    @Test
    void excluirDeveDelegarParaRepositorio() {
        service.excluir(22L);
        verify(questaoRepository).deleteById(22L);
    }
}

