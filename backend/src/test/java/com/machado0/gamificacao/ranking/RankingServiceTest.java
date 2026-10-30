package com.machado0.gamificacao.ranking;

import com.machado0.gamificacao.turma.Turma;
import com.machado0.gamificacao.turma.TurmaAluno;
import com.machado0.gamificacao.turma.TurmaAlunoRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {
    @Mock private TurmaAlunoRepository repository;

    @Test
    void deveOrdenarRankingPorXpEDesempatarPorId() {
        Usuario aluno = usuario(1L, "Ana", 100);
        Usuario outro = usuario(2L, "Bia", 100);
        Turma turma = new Turma();
        turma.setId(7L);
        turma.setNome("1A");
        turma.setAtivo(true);
        TurmaAluno vinculo = vinculo(turma, aluno);
        when(repository.findFirstByUsuario(aluno)).thenReturn(Optional.of(vinculo));
        when(repository.findByTurma(turma)).thenReturn(List.of(vinculo, vinculo(turma, outro)));

        RankingResponseDTO response = new RankingService(repository).buscar(aluno);

        assertEquals(2, response.alunos().size());
        assertEquals(1L, response.alunos().get(0).usuarioId());
        assertEquals(1, response.meuPerfil().posicao());
    }

    @Test
    void deveRejeitarAlunoSemTurma() {
        Usuario aluno = usuario(1L, "Ana", 100);
        when(repository.findFirstByUsuario(aluno)).thenReturn(Optional.empty());

        assertThrows(RankingNaoDisponivelException.class,
                () -> new RankingService(repository).buscar(aluno));
    }

    private Usuario usuario(Long id, String nome, int xp) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setXp(xp);
        usuario.setNivel(2);
        usuario.setPerfil(Perfil.ALUNO);
        return usuario;
    }

    private TurmaAluno vinculo(Turma turma, Usuario usuario) {
        TurmaAluno vinculo = new TurmaAluno();
        vinculo.setTurma(turma);
        vinculo.setUsuario(usuario);
        return vinculo;
    }
}
