package com.machado0.gamificacao.missao;

import java.time.OffsetDateTime;

public record MissaoDiariaDTO(
        Long id,
        String titulo,
        TipoMissao tipo,
        int meta,
        int progressoAtual,
        int recompensaXp,
        boolean concluida,
        OffsetDateTime dataReferencia,
        OffsetDateTime dataConclusao) {
}
