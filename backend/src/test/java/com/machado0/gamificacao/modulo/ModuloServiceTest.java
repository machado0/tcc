package com.machado0.gamificacao.modulo;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class ModuloServiceTest {

    private ModuloRepository moduloRepository = Mockito.mock(ModuloRepository.class);
    private DtoMapper mapper;
    private ModuloService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class), moduloRepository,
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new ModuloService(moduloRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Modulo salvo = new Modulo();
        salvo.setId(11L);
        salvo.setNome("Introdução à Álgebra");
        salvo.setDescricao("Conceitos básicos de álgebra para iniciantes");

        when(moduloRepository.save(any(Modulo.class))).thenReturn(salvo);

        ModuloDto dto = new ModuloDto();
        dto.setNome("Introdução à Álgebra");
        dto.setDescricao("Conceitos básicos de álgebra para iniciantes");

        ModuloDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(11L, out.getId());
        assertEquals("Introdução à Álgebra", out.getNome());
    }

    @Test
    void excluirDeveDelegarParaRepositorio() {
        service.excluir(7L);
        verify(moduloRepository).deleteById(7L);
    }
}

