package com.machado0.gamificacao.dashboard;

import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    @Mock private com.machado0.gamificacao.tentativa.TentativaAlunoRepository tentativaRepository;
    @Mock private com.machado0.gamificacao.turma.TurmaAlunoRepository turmaAlunoRepository;

    @Test
    void deveRetornarEstatisticasDoAluno() {
        Usuario aluno = new Usuario();
        aluno.setPerfil(Perfil.ALUNO);
        aluno.setNivel(2);
        aluno.setXp(150);
        aluno.setStreak(3);
        aluno.setBestStreak(5);
        aluno.setMoedas(20);
        aluno.setVidas(2);

        DashboardStatsDTO dashboard = new DashboardService(tentativaRepository, turmaAlunoRepository).buscar(aluno);

        assertEquals(2, dashboard.level());
        assertEquals(150, dashboard.currentXp());
        assertEquals(200, dashboard.nextLevelXp());
        assertEquals(3, dashboard.streak());
        assertEquals(5, dashboard.bestStreak());
        assertEquals(20, dashboard.coins());
        assertEquals(2, dashboard.lives());
    }

    @Test
    void deveRecusarProfessor() {
        Usuario professor = new Usuario();
        professor.setPerfil(Perfil.PROFESSOR);

        assertThrows(DashboardAcessoNegadoException.class,
                () -> new DashboardService(tentativaRepository, turmaAlunoRepository).buscar(professor));
    }
}
