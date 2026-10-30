package com.machado0.gamificacao.dashboard;

import com.machado0.gamificacao.missao.MissaoDiariaDTO;
import java.util.List;

public record DashboardStatsDTO(
        Integer level,
        Integer currentXp,
        Integer nextLevelXp,
        Integer xpToday,
        Integer streak,
        Integer bestStreak,
        Integer coins,
        Integer lives,
        Integer posicaoRanking,
        Integer questoesRespondidas,
        Integer questoesCorretas,
        Integer aulasConcluidas,
        Integer tempoTotalEstudoMinutos,
        List<MissaoDiariaDTO> missoesDiarias,
        ProgressoSemanalDTO progressoSemanal) {
}
