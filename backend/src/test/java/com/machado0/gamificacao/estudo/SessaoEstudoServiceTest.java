package com.machado0.gamificacao.estudo;

import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.aula.AulaRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SessaoEstudoServiceTest {
    private SessaoEstudoRepository repository;
    private AulaRepository aulaRepository;
    private SessaoEstudoService service;
    private Usuario aluno;
    private Aula aula;

    @BeforeEach
    void setUp() {
        repository = mock(SessaoEstudoRepository.class);
        aulaRepository = mock(AulaRepository.class);
        service = new SessaoEstudoService(repository, aulaRepository);
        aluno = new Usuario();
        aluno.setId(3L);
        aluno.setPerfil(Perfil.ALUNO);
        aula = new Aula();
        aula.setId(10L);
        when(aulaRepository.findById(10L)).thenReturn(Optional.of(aula));
        when(repository.save(any(SessaoEstudo.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void iniciarReutilizaSessaoAbertaDaMesmaAula() {
        SessaoEstudo existente = novaSessao(42L, false);
        when(repository.findFirstByUsuarioAndAula_IdAndConcluidaFalseOrderByInicioAsc(aluno, 10L))
                .thenReturn(Optional.of(existente));

        SessaoEstudoResponse resposta = service.iniciar(aluno, new IniciarSessaoRequest(10L));

        assertEquals(42L, resposta.id());
        verify(repository, never()).save(any(SessaoEstudo.class));
    }

    @Test
    void iniciarCriaNovaSessaoQuandoNaoHaSessaoAberta() {
        when(repository.findFirstByUsuarioAndAula_IdAndConcluidaFalseOrderByInicioAsc(aluno, 10L))
                .thenReturn(Optional.empty());

        SessaoEstudoResponse resposta = service.iniciar(aluno, new IniciarSessaoRequest(10L));

        assertEquals(10L, resposta.aulaId());
        assertTrue(!resposta.concluida());
        verify(repository).save(any(SessaoEstudo.class));
    }

    @Test
    void finalizarSessaoJaConcluidaNaoPersisteNovamente() {
        SessaoEstudo existente = novaSessao(42L, true);
        existente.setFim(existente.getInicio().plusMinutes(5));
        existente.setDuracaoMinutos(5);
        when(repository.findByIdAndUsuario(42L, aluno)).thenReturn(Optional.of(existente));

        SessaoEstudoResponse resposta = service.finalizar(aluno, 42L);

        assertTrue(resposta.concluida());
        assertEquals(5, resposta.duracaoMinutos());
        verify(repository, never()).save(any(SessaoEstudo.class));
    }

    private SessaoEstudo novaSessao(Long id, boolean concluida) {
        SessaoEstudo sessao = new SessaoEstudo();
        sessao.setId(id);
        sessao.setUsuario(aluno);
        sessao.setAula(aula);
        sessao.setInicio(LocalDateTime.now().minusMinutes(5));
        sessao.setDataCriacao(LocalDateTime.now().minusMinutes(5));
        sessao.setConcluida(concluida);
        return sessao;
    }
}
