-- V2: Templates de missões e missões diárias

CREATE TABLE templates_missoes (
    id                  BIGSERIAL    PRIMARY KEY,
    tipo                VARCHAR(40)  NOT NULL,
    texto               VARCHAR(120) NOT NULL,
    metas_possiveis     INT[]        NOT NULL,
    recompensa_base     INT          NOT NULL,
    icone               VARCHAR(50),
    ativo               BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_template_tipo CHECK (tipo IN ('ACERTAR_QUESTOES','RESPONDER_QUESTOES','CONCLUIR_AULAS','OBTER_XP')),
    CONSTRAINT ck_template_metas CHECK (array_length(metas_possiveis, 1) >= 1)
);

CREATE TABLE missoes_usuario (
    id               BIGSERIAL PRIMARY KEY,
    template_id      BIGINT       NOT NULL REFERENCES templates_missoes(id),
    usuario_id       BIGINT       NOT NULL UNIQUE REFERENCES usuarios(id) ON DELETE CASCADE,
    data_referencia  TIMESTAMP WITH TIME ZONE NOT NULL,
    texto            VARCHAR(150) NOT NULL,
    meta             INT          NOT NULL,
    progresso        INT          NOT NULL DEFAULT 0,
    recompensa    INT          NOT NULL,
    concluida        BOOLEAN      NOT NULL DEFAULT FALSE,
    data_conclusao   TIMESTAMP WITH TIME ZONE,
    CONSTRAINT uk_usuario_template_dia UNIQUE (usuario_id, template_id, data_referencia),
    CONSTRAINT ck_missao_meta CHECK (meta > 0)
);

CREATE INDEX idx_missoes_usuario_dia ON missoes_usuario (usuario_id, data_referencia);
