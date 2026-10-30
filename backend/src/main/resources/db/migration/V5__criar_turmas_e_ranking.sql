-- V5: Turmas, vinculo de alunos e ranking semanal

CREATE TABLE turmas (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    ano_letivo INT          NOT NULL DEFAULT 2026,
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE turma_alunos (
    id          BIGSERIAL PRIMARY KEY,
    turma_id    BIGINT NOT NULL REFERENCES turmas(id)   ON DELETE CASCADE,
    usuario_id  BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT uk_turma_usuario UNIQUE (turma_id, usuario_id)
);

CREATE TABLE ranking_semanal (
    id                   BIGSERIAL PRIMARY KEY,
    turma_id             BIGINT NOT NULL REFERENCES turmas(id)   ON DELETE CASCADE,
    usuario_id           BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    ano                  INT    NOT NULL,
    numero_semana        INT    NOT NULL, -- 1 a 53
    xp_acumulado_semana  INT    NOT NULL DEFAULT 0,
    posicao              INT,
    variacao_posicoes    INT    NOT NULL DEFAULT 0,
    CONSTRAINT uk_ranking_turma_aluno_semana UNIQUE (turma_id, usuario_id, ano, numero_semana),
    CONSTRAINT ck_ranking_semana CHECK (numero_semana BETWEEN 1 AND 53)
);

CREATE INDEX idx_turma_alunos_usuario ON turma_alunos (usuario_id);
CREATE INDEX idx_ranking_turma_semana ON ranking_semanal (turma_id, ano, numero_semana, xp_acumulado_semana DESC);
