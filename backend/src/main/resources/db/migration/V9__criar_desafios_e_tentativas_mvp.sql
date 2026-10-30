-- V9: modelo de desafios

CREATE TABLE cursos (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   VARCHAR(250) NOT NULL,
    icone       VARCHAR(100)
);

ALTER TABLE questoes
    ADD COLUMN cabecalho VARCHAR(500),
    ADD COLUMN dificuldade VARCHAR(20) NOT NULL DEFAULT 'FACIL',
    ADD COLUMN xp_recompensa INT NOT NULL DEFAULT 10,
    ADD COLUMN curso_id BIGINT;

UPDATE questoes
SET cabecalho = COALESCE(cabecalho, enunciado);

ALTER TABLE questoes
    ALTER COLUMN cabecalho SET NOT NULL,
    ADD CONSTRAINT fk_questao_curso
        FOREIGN KEY (curso_id) REFERENCES cursos(id),
    ADD CONSTRAINT ck_questao_dificuldade
        CHECK (dificuldade IN ('FACIL', 'MEDIO', 'DIFICIL')),
    ADD CONSTRAINT ck_questao_xp
        CHECK (xp_recompensa >= 0);

CREATE TABLE alternativas (
    id              BIGSERIAL PRIMARY KEY,
    questao_id      BIGINT NOT NULL REFERENCES questoes(id) ON DELETE CASCADE,
    texto           VARCHAR(250) NOT NULL,
    justificativa   VARCHAR(500) NOT NULL,
    correta         BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE tentativas_aluno (
    id                       BIGSERIAL PRIMARY KEY,
    usuario_id               BIGINT NOT NULL REFERENCES usuarios(id),
    questao_id               BIGINT NOT NULL REFERENCES questoes(id),
    alternativa_escolhida_id BIGINT NOT NULL REFERENCES alternativas(id),
    acertou                  BOOLEAN NOT NULL,
    data_tentativa            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tentativa_usuario_questao
    ON tentativas_aluno(usuario_id, questao_id, acertou);
