-- V6: Questoes, alternativas e configuracoes aluno

CREATE TABLE questoes (
    id                     BIGSERIAL PRIMARY KEY,
    aula_id                BIGINT NOT NULL REFERENCES aulas(id) ON DELETE CASCADE,
    ordem                  INT    NOT NULL,
    enunciado              TEXT   NOT NULL,
    contexto               VARCHAR(100),
    opcao_correta_id       BIGINT,
    explicacao             TEXT   NOT NULL,
    recompensa             INT    NOT NULL DEFAULT 25,
    CONSTRAINT uk_questao_ordem UNIQUE (aula_id, ordem)
);

CREATE TABLE alternativas_questao (
    id          BIGSERIAL PRIMARY KEY,
    questao_id  BIGINT     NOT NULL REFERENCES questoes(id) ON DELETE CASCADE,
    letra       VARCHAR(1) NOT NULL,
    texto       TEXT       NOT NULL,
    CONSTRAINT uk_alternativa_letra UNIQUE (questao_id, letra)
);

ALTER TABLE questoes
    ADD CONSTRAINT fk_questao_alternativa_correta
    FOREIGN KEY (opcao_correta_id) REFERENCES alternativas_questao(id)
    DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE configuracoes_aluno (
    id                      BIGSERIAL PRIMARY KEY,
    usuario_id              BIGINT       NOT NULL UNIQUE REFERENCES usuarios(id) ON DELETE CASCADE,
    matricula               VARCHAR(30)  NOT NULL UNIQUE,
    escola_nome             VARCHAR(150) NOT NULL,
    email_responsaveis      VARCHAR(150),
    tema                    VARCHAR(20)  NOT NULL DEFAULT 'sistema',
    tamanho_fonte           VARCHAR(20)  NOT NULL DEFAULT 'padrao',
    alto_contraste          BOOLEAN      NOT NULL DEFAULT FALSE,
    efeitos_sonoros         BOOLEAN      NOT NULL DEFAULT TRUE,
    animacoes               BOOLEAN      NOT NULL DEFAULT TRUE,
    notificar_tarefas       BOOLEAN      NOT NULL DEFAULT TRUE,
    notificar_provas        BOOLEAN      NOT NULL DEFAULT TRUE,
    resumo_semanal_pais     BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_config_tema  CHECK (tema IN ('claro','escuro','sistema')),
    CONSTRAINT ck_config_fonte CHECK (tamanho_fonte IN ('padrao','grande'))
);

CREATE INDEX idx_questoes_aula ON questoes (aula_id, ordem);
CREATE INDEX idx_alternativas_questao ON alternativas_questao (questao_id);
