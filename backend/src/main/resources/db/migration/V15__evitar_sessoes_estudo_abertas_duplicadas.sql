WITH duplicadas AS (
    SELECT id,
           ROW_NUMBER() OVER (PARTITION BY usuario_id, aula_id ORDER BY inicio, id) AS ordem
    FROM sessoes_estudo
    WHERE concluida = FALSE
      AND aula_id IS NOT NULL
)
UPDATE sessoes_estudo s
SET concluida = TRUE,
    fim = COALESCE(s.fim, CURRENT_TIMESTAMP),
    duracao_minutos = GREATEST(
        1,
        FLOOR(EXTRACT(EPOCH FROM (COALESCE(s.fim, CURRENT_TIMESTAMP) - s.inicio)) / 60)::INTEGER
    )
FROM duplicadas d
WHERE s.id = d.id
  AND d.ordem > 1;

CREATE UNIQUE INDEX IF NOT EXISTS uq_sessoes_estudo_usuario_aula_aberta
    ON sessoes_estudo (usuario_id, aula_id)
    WHERE concluida = FALSE AND aula_id IS NOT NULL;
