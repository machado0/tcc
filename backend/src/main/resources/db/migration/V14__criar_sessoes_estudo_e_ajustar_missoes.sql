CREATE TABLE sessoes_estudo (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    aula_id BIGINT REFERENCES aulas(id),
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP,
    duracao_minutos INT,
    concluida BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_sessao_duracao CHECK (duracao_minutos IS NULL OR duracao_minutos BETWEEN 1 AND 720),
    CONSTRAINT ck_sessao_fim CHECK (fim IS NULL OR fim >= inicio)
);

CREATE INDEX idx_sessoes_estudo_usuario_inicio
    ON sessoes_estudo (usuario_id, inicio);

ALTER TABLE missoes_usuario
    DROP CONSTRAINT IF EXISTS missoes_usuario_usuario_id_key;

ALTER TABLE progresso_aula_usuario
    ADD COLUMN data_atualizacao TIMESTAMP;
