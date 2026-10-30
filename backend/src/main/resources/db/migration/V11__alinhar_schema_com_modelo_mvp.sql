-- V11: colunas de transicao para o modelo MVP ativo.

ALTER TABLE estatisticas_usuario
    ADD COLUMN xp_proximo_nivel BIGINT NOT NULL DEFAULT 500;

ALTER TABLE materias
    ADD COLUMN ordem INT NOT NULL DEFAULT 0;

ALTER TABLE progresso_aula_usuario
    ADD COLUMN percentual_conclusao INT NOT NULL DEFAULT 0;

ALTER TABLE templates_missoes
    ADD COLUMN tipo_missao VARCHAR(40),
    ADD COLUMN xp_recompensa_base INT NOT NULL DEFAULT 0;

UPDATE templates_missoes
SET tipo_missao = tipo,
    xp_recompensa_base = recompensa_base
WHERE tipo_missao IS NULL;

ALTER TABLE modulos
    ADD COLUMN nome VARCHAR(120);

UPDATE modulos
SET nome = titulo
WHERE nome IS NULL;

ALTER TABLE modulos
    ALTER COLUMN nome SET NOT NULL;

CREATE TABLE metas (
    template_id BIGINT NOT NULL REFERENCES templates_missoes(id) ON DELETE CASCADE,
    metas_possiveis INT NOT NULL
);

CREATE TABLE IF NOT EXISTS cursos (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   VARCHAR(250) NOT NULL,
    icone       VARCHAR(100)
);

ALTER TABLE modulos
    ADD COLUMN curso_id BIGINT;

INSERT INTO cursos (nome, descricao, icone)
SELECT titulo, COALESCE(descricao, titulo), icone
FROM materias
WHERE NOT EXISTS (SELECT 1 FROM cursos WHERE cursos.nome = materias.titulo);

UPDATE modulos m
SET curso_id = c.id
FROM materias materia
JOIN cursos c ON c.nome = materia.titulo
WHERE m.materia_id = materia.id;

ALTER TABLE modulos
    ADD CONSTRAINT fk_modulo_curso FOREIGN KEY (curso_id) REFERENCES cursos(id);

ALTER TABLE aulas
    ADD COLUMN nome VARCHAR(100);

UPDATE aulas
SET nome = titulo
WHERE nome IS NULL;

ALTER TABLE aulas
    ALTER COLUMN nome SET NOT NULL;

ALTER TABLE conquistas
    ADD COLUMN modulo_id BIGINT,
    ADD CONSTRAINT fk_conquista_modulo
        FOREIGN KEY (modulo_id) REFERENCES modulos(id);
