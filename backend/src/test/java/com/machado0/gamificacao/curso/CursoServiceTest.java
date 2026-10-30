package com.machado0.gamificacao.curso;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class CursoServiceTest {

    private CursoRepository cursoRepository = Mockito.mock(CursoRepository.class);
    private DtoMapper mapper;
    private CursoService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(cursoRepository, Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new CursoService(cursoRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Curso salvo = new Curso();
        salvo.setId(100L);
        salvo.setNome("Matemática Básica");
        salvo.setDescricao("Curso introdutório sobre operações básicas e conceitos fundamentais");

        when(cursoRepository.save(any(Curso.class))).thenReturn(salvo);

        CursoDto dto = new CursoDto();
        dto.setNome("Matemática Básica");
        dto.setDescricao("Curso introdutório sobre operações básicas e conceitos fundamentais");

        CursoDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(100L, out.getId());
        assertEquals("Matemática Básica", out.getNome());
    }

    @Test
    void excluirShouldCallRepository() {
        service.excluir(1L);
        verify(cursoRepository).deleteById(1L);
    }
}

