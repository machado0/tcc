-- V8: identidade, perfil e estado do usuario

ALTER TABLE usuarios
    ADD COLUMN senha VARCHAR(255),
    ADD COLUMN perfil VARCHAR(20),
    ADD COLUMN professor_id BIGINT,
    ADD COLUMN xp INT NOT NULL DEFAULT 0,
    ADD COLUMN nivel INT NOT NULL DEFAULT 1,
    ADD COLUMN vidas INT NOT NULL DEFAULT 3,
    ADD COLUMN moedas INT NOT NULL DEFAULT 0,
    ADD COLUMN streak INT NOT NULL DEFAULT 0,
    ADD COLUMN best_streak INT NOT NULL DEFAULT 0,
    ADD COLUMN ultimo_acesso TIMESTAMP,
    ADD COLUMN data_atualizacao TIMESTAMP;

UPDATE usuarios
SET perfil = 'ALUNO',
    senha = COALESCE(senha, '')
WHERE perfil IS NULL;

ALTER TABLE usuarios
    ALTER COLUMN senha SET NOT NULL,
    ALTER COLUMN perfil SET NOT NULL,
    ADD CONSTRAINT fk_usuario_professor
        FOREIGN KEY (professor_id) REFERENCES usuarios(id),
    ADD CONSTRAINT ck_usuario_perfil
        CHECK (perfil IN ('ALUNO', 'PROFESSOR')),
    ADD CONSTRAINT ck_usuario_xp
        CHECK (xp >= 0),
    ADD CONSTRAINT ck_usuario_nivel
        CHECK (nivel >= 1),
    ADD CONSTRAINT ck_usuario_vidas
        CHECK (vidas >= 0),
    ADD CONSTRAINT ck_usuario_moedas
        CHECK (moedas >= 0),
    ADD CONSTRAINT ck_usuario_streak
        CHECK (streak >= 0),
    ADD CONSTRAINT ck_usuario_best_streak
        CHECK (best_streak >= 0);

CREATE INDEX idx_usuarios_professor ON usuarios(professor_id);
CREATE INDEX idx_usuarios_ranking ON usuarios(professor_id, xp DESC, id);
