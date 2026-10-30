CREATE TABLE configuracoes_usuario (
    usuario_id BIGINT PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
    matricula VARCHAR(80),
    escola VARCHAR(160),
    turma VARCHAR(120),
    email_responsaveis VARCHAR(200),
    tema VARCHAR(20) NOT NULL DEFAULT 'sistema',
    tamanho_fonte VARCHAR(20) NOT NULL DEFAULT 'padrao',
    alto_contraste BOOLEAN NOT NULL DEFAULT FALSE,
    efeitos_sonoros BOOLEAN NOT NULL DEFAULT TRUE,
    animacoes_gamificacao BOOLEAN NOT NULL DEFAULT TRUE,
    notificar_tarefas BOOLEAN NOT NULL DEFAULT TRUE,
    notificar_provas BOOLEAN NOT NULL DEFAULT TRUE,
    resumo_semanal_pais BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE notificacoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    titulo VARCHAR(160) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rota VARCHAR(200)
);

CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_id, criada_em DESC);
