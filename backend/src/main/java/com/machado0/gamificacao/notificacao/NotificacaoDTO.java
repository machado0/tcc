package com.machado0.gamificacao.notificacao;
import java.time.LocalDateTime;
public record NotificacaoDTO(Long id, String titulo, String mensagem, String tipo, boolean lida, LocalDateTime criadaEm, String rota) {
    static NotificacaoDTO from(Notificacao n) { return new NotificacaoDTO(n.getId(), n.getTitulo(), n.getMensagem(), n.getTipo(), n.isLida(), n.getCriadaEm(), n.getRota()); }
}
