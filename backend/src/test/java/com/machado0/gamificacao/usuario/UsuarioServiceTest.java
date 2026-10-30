package com.machado0.gamificacao.usuario;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
    private DtoMapper mapper;
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class),
                Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                usuarioRepository);
        service = new UsuarioService(usuarioRepository, mapper);
    }

    @Test
    void salvarDeveMapearERetornarDto() {
        Usuario salvo = new Usuario();
        salvo.setId(5L);
        salvo.setNome("Carlos Silva");
        salvo.setEmail("carlos.silva@example.com");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(salvo);

        UsuarioDto dto = new UsuarioDto();
        dto.setNome("Carlos Silva");
        dto.setEmail("carlos.silva@example.com");
        dto.setSenha("senhaSegura123");

        UsuarioDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(5L, out.getId());
        assertEquals("carlos.silva@example.com", out.getEmail());
    }

    @Test
    void excluirShouldCallRepository() {
        service.excluir(3L);
        verify(usuarioRepository).deleteById(3L);
    }
}

