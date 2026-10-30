-- V4: Materias, modulos, aulas e progresso

CREATE TABLE materias (
    id         BIGSERIAL PRIMARY KEY,
    titulo     VARCHAR(100) NOT NULL,
    descricao  TEXT,
    icone      VARCHAR(50)  DEFAULT 'school',
    cor        VARCHAR(30)  DEFAULT 'primary',
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE modulos (
    id          BIGSERIAL PRIMARY KEY,
    materia_id  BIGINT       NOT NULL REFERENCES materias(id) ON DELETE CASCADE,
    ordem       INT          NOT NULL,
    titulo      VARCHAR(120) NOT NULL,
    descricao   TEXT,
    CONSTRAINT uk_modulo_ordem UNIQUE (materia_id, ordem)
);

CREATE TABLE aulas (
    id               BIGSERIAL PRIMARY KEY,
    modulo_id        BIGINT       NOT NULL REFERENCES modulos(id) ON DELETE CASCADE,
    ordem            INT          NOT NULL,
    titulo           VARCHAR(150) NOT NULL,
    descricao        TEXT,
    duracao_minutos  INT          NOT NULL DEFAULT 15,
    recompensa       INT          NOT NULL DEFAULT 30,
    conteudo         TEXT,
    pontos_chave     TEXT,
    dica_professor   TEXT,
    CONSTRAINT uk_aula_ordem UNIQUE (modulo_id, ordem)
);

CREATE TABLE progresso_aula_usuario (
    id              BIGSERIAL PRIMARY KEY,
    usuario_id      BIGINT      NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    aula_id         BIGINT      NOT NULL REFERENCES aulas(id) ON DELETE CASCADE,
    status          VARCHAR(20) NOT NULL DEFAULT 'CONCLUIDA',
    data_conclusao  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_usuario_aula UNIQUE (usuario_id, aula_id),
    CONSTRAINT ck_progresso_status CHECK (status IN ('CONCLUIDA','DISPONIVEL'))
);

CREATE INDEX idx_modulos_materia ON modulos (materia_id, ordem);
CREATE INDEX idx_aulas_modulo ON aulas (modulo_id, ordem);
CREATE INDEX idx_progresso_usuario ON progresso_aula_usuario (usuario_id);
