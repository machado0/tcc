-- V7: Dados de referencia.

INSERT INTO templates_missoes (tipo, texto, metas_possiveis, recompensa_base, icone) VALUES
 ('ACERTAR_QUESTOES',   'Acertar {meta} questões',    ARRAY[3,5,8],  10, 'check_circle'),
 ('RESPONDER_QUESTOES', 'Responder {meta} questões',  ARRAY[5,10,15], 3, 'quiz'),
 ('CONCLUIR_AULAS',     'Concluir {meta} aulas',      ARRAY[1,2,3],  30, 'menu_book'),
 ('OBTER_XP',           'Obter {meta} XP hoje',       ARRAY[50,100,150], 1, 'bolt');

INSERT INTO conquistas
    (codigo, titulo, descricao, icone, meta, recompensa_xp, moedas_recompensa, tipo)
VALUES
 ('PRIMEIRA_FACANHA', 'Primeira Façanha', 'Conclua sua primeira aula na plataforma', 'military_tech', 1, 50, 10, 'GERAL'),
 ('ESTUDIOSO', 'Estudioso', 'Conclua 10 aulas', 'school', 10, 100, 20, 'QUESTOES'),
 ('SEQUENCIA_7', 'Sequência de Fogo', 'Alcance uma sequência de 7 dias de estudo', 'local_fire_department', 7, 100, 25, 'OFENSIVA'),
 ('SEQUENCIA_14', 'Mestre da Constância', 'Alcance uma sequência de 14 dias de estudo', 'stars', 14, 200, 50, 'OFENSIVA'),
 ('CAMPEAO_TURMA', 'Campeão da Turma', 'Termine uma semana no top 3 do ranking', 'emoji_events', 1, 150, 30, 'GERAL');
