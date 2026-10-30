package com.machado0.gamificacao.alternativa;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AlternativaServiceTest {

    private AlternativaRepository alternativaRepository = Mockito.mock(AlternativaRepository.class);
    private DtoMapper mapper;
    private AlternativaService service;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(Mockito.mock(com.machado0.gamificacao.curso.CursoRepository.class),
                Mockito.mock(com.machado0.gamificacao.modulo.ModuloRepository.class),
                Mockito.mock(com.machado0.gamificacao.aula.AulaRepository.class),
                Mockito.mock(com.machado0.gamificacao.questao.QuestaoRepository.class),
                Mockito.mock(com.machado0.gamificacao.usuario.UsuarioRepository.class));
        service = new AlternativaService(alternativaRepository, mapper);
    }

    @Test
    void salvarDeveRetornarDtoComId() {
        Alternativa salva = new Alternativa();
        salva.setId(41L);
        salva.setTexto("12");

        when(alternativaRepository.save(any(Alternativa.class))).thenReturn(salva);

        AlternativaDto dto = new AlternativaDto();
        dto.setTexto("12");
        dto.setJustificativa("Soma de 7 e 5");
        dto.setCorreta(true);

        AlternativaDto out = service.salvar(dto);
        assertNotNull(out);
        assertEquals(41L, out.getId());
        assertEquals("12", out.getTexto());
    }

    @Test
    void excluirDeveDelegarParaRepositorio() {
        service.excluir(15L);
        verify(alternativaRepository).deleteById(15L);
    }
}

