package com.machado0.gamificacao.progresso;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class ProgressoServiceTest {

    private ProgressoRepository progressoRepository = Mockito.mock(ProgressoRepository.class);
    private DtoMapper mapper;
    private ProgressoService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class),
                Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new ProgressoService(progressoRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Progresso salvo = new Progresso();
        salvo.setId(51L);

        when(progressoRepository.save(any(Progresso.class))).thenReturn(salvo);

        ProgressoDto dto = new ProgressoDto();
        dto.setUsuarioId(2L);
        dto.setAulaId(4L);
        dto.setPercentualConclusao(25);

        ProgressoDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(51L, out.getId());
    }

    @Test
    void excluirDeveDelegarParaRepositorio() {
        service.excluir(6L);
        verify(progressoRepository).deleteById(6L);
    }
}

