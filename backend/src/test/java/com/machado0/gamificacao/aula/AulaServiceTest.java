package com.machado0.gamificacao.aula;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AulaServiceTest {

    private AulaRepository aulaRepository = Mockito.mock(AulaRepository.class);
    private DtoMapper mapper;
    private AulaService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class),
                Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new AulaService(aulaRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Aula salva = new Aula();
        salva.setId(21L);
        salva.setNome("Aula 1: Números e Operações");
        salva.setDescricao("Introdução aos números inteiros e às operações básicas");

        when(aulaRepository.save(any(Aula.class))).thenReturn(salva);

        AulaDto dto = new AulaDto();
        dto.setNome("Aula 1: Números e Operações");
        dto.setDescricao("Introdução aos números inteiros e às operações básicas");

        AulaDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(21L, out.getId());
        assertEquals("Aula 1: Números e Operações", out.getNome());
    }

    @Test
    void excluirDeveDelegarParaRepositorio() {
        service.excluir(9L);
        verify(aulaRepository).deleteById(9L);
    }
}

