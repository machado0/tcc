-- V3: Conquistas e progresso por usuario

CREATE TABLE conquistas (
    id             BIGSERIAL PRIMARY KEY,
    codigo         VARCHAR(80)  NOT NULL UNIQUE,
    titulo         VARCHAR(100) NOT NULL,
    descricao      TEXT         NOT NULL,
    icone          VARCHAR(50)  NOT NULL,
    meta           INT          NOT NULL,
    recompensa_xp  INT          NOT NULL,
    moedas_recompensa INT NOT NULL DEFAULT 0,
    tipo           VARCHAR(30)  NOT NULL,
    CONSTRAINT ck_conquista_tipo CHECK (tipo IN ('GERAL','QUESTOES','ACERTOS','OFENSIVA','TEMPO'))
);

CREATE TABLE conquistas_usuario (
    id                BIGSERIAL PRIMARY KEY,
    usuario_id        BIGINT  NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    conquista_id      BIGINT  NOT NULL REFERENCES conquistas(id),
    progresso         INT     NOT NULL DEFAULT 0,
    desbloqueada      BOOLEAN NOT NULL DEFAULT FALSE,
    data_desbloqueio  TIMESTAMP,
    CONSTRAINT uk_usuario_conquista UNIQUE (usuario_id, conquista_id)
);

CREATE INDEX idx_conquistas_usuario_usuario ON conquistas_usuario (usuario_id);
