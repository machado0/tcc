package com.machado0.gamificacao.conquista;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConquistaServiceTest {

    private ConquistaRepository conquistaRepository = Mockito.mock(ConquistaRepository.class);
    private DtoMapper mapper;
    private ConquistaService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class),
                Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new ConquistaService(conquistaRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Conquista salva = new Conquista();
        salva.setId(77L);
        salva.setNome("Concluiu Módulo 1");

        when(conquistaRepository.save(any(Conquista.class))).thenReturn(salva);

        ConquistaDto dto = new ConquistaDto();
        dto.setNome("Concluiu Módulo 1");

        ConquistaDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(77L, out.getId());
        assertEquals("Concluiu Módulo 1", out.getNome());
    }

    @Test
    void excluirDeveDelegarParaRepositorio() {
        service.excluir(77L);
        verify(conquistaRepository).deleteById(77L);
    }
}

