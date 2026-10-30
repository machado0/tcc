-- V1: Usuarios, estatisticas e metricas

CREATE TABLE usuarios (
    id            BIGSERIAL PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    titulo_honra  VARCHAR(80)  DEFAULT 'Estudante Dedicado',
    data_criacao  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE estatisticas_usuario (
    id                      BIGSERIAL PRIMARY KEY,
    usuario_id              BIGINT NOT NULL UNIQUE REFERENCES usuarios(id) ON DELETE CASCADE,
    nivel                   INT    NOT NULL DEFAULT 1,
    xp_atual                BIGINT NOT NULL DEFAULT 0,
    xp_hoje                 INT    NOT NULL DEFAULT 0,
    sequencia_dias          INT    NOT NULL DEFAULT 0,
    melhor_sequencia_dias   INT    NOT NULL DEFAULT 0,
    moedas                  INT    NOT NULL DEFAULT 0,
    vidas                   INT    NOT NULL DEFAULT 3,
    ultima_data_estudo      TIMESTAMP,
    ultima_vida_perdida_em  TIMESTAMP,
    CONSTRAINT ck_estatisticas_vidas CHECK (vidas >= 0),
    CONSTRAINT ck_estatisticas_moedas CHECK (moedas >= 0)
);

CREATE TABLE metricas_desempenho (
    id                          BIGSERIAL PRIMARY KEY,
    usuario_id                  BIGINT NOT NULL UNIQUE REFERENCES usuarios(id) ON DELETE CASCADE,
    questoes_respondidas        INT NOT NULL DEFAULT 0,
    questoes_corretas           INT NOT NULL DEFAULT 0,
    aulas_concluidas            INT NOT NULL DEFAULT 0,
    tempo_total_estudo_minutos  INT NOT NULL DEFAULT 0,
    CONSTRAINT ck_metricas_corretas CHECK (questoes_corretas <= questoes_respondidas)
);
