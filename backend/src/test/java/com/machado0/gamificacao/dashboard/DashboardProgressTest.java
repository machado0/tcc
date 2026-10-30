package com.machado0.gamificacao.dashboard;

import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.tentativa.TentativaAluno;
import com.machado0.gamificacao.tentativa.TentativaAlunoRepository;
import com.machado0.gamificacao.turma.*;
import com.machado0.gamificacao.usuario.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardProgressTest {
    @Mock TentativaAlunoRepository tentativaRepository;
    @Mock TurmaAlunoRepository turmaRepository;

    @Test
    void deveCalcularXpDoDiaEPosicaoNaTurma() {
        Usuario aluno = usuario(1L, 100);
        Usuario outro = usuario(2L, 200);
        Questao questao = new Questao();
        questao.setId(3L);
        questao.setXpRecompensa(15);
        TentativaAluno tentativa = new TentativaAluno();
        tentativa.setQuestao(questao);
        tentativa.setDataTentativa(LocalDateTime.now());
        tentativa.setAcertou(true);
        Turma turma = new Turma();
        turma.setId(5L);
        turma.setAtivo(true);
        TurmaAluno vinculo = vinculo(turma, aluno);
        when(tentativaRepository.findByUsuarioAndAcertouTrue(aluno)).thenReturn(List.of(tentativa));
        when(turmaRepository.findFirstByUsuario(aluno)).thenReturn(Optional.of(vinculo));
        when(turmaRepository.findByTurma(turma)).thenReturn(List.of(vinculo, vinculo(turma, outro)));

        DashboardStatsDTO response = new DashboardService(tentativaRepository, turmaRepository).buscar(aluno);

        assertEquals(15, response.xpToday());
        assertEquals(2, response.posicaoRanking());
    }

    private Usuario usuario(Long id, int xp) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setXp(xp);
        usuario.setNivel(2);
        usuario.setPerfil(Perfil.ALUNO);
        return usuario;
    }

    private TurmaAluno vinculo(Turma turma, Usuario usuario) {
        TurmaAluno item = new TurmaAluno();
        item.setTurma(turma);
        item.setUsuario(usuario);
        return item;
    }
}
