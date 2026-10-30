package com.machado0.gamificacao.desafio;

import com.machado0.gamificacao.conquista.ConquistaDesbloqueadaDTO;
import java.util.List;

public record FeedbackDTO(
        boolean acertou,
        Long alternativaCorretaId,
        String justificativa,
        Integer xpGanho,
        Integer moedasGanha,
        Integer vidasRestantes,
        Integer novoXpTotal,
        Integer novoNivel,
        List<ConquistaDesbloqueadaDTO> conquistasDesbloqueadas) {

    public FeedbackDTO(boolean acertou, Long alternativaCorretaId, String justificativa,
                       Integer xpGanho, Integer moedasGanha, Integer vidasRestantes,
                       Integer novoXpTotal, Integer novoNivel) {
        this(acertou, alternativaCorretaId, justificativa, xpGanho, moedasGanha,
                vidasRestantes, novoXpTotal, novoNivel, List.of());
    }
}
