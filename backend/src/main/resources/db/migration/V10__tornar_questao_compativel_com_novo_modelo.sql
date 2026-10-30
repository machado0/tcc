-- V10: tornar a tabela de questoes compativel com as alteracoes

ALTER TABLE questoes
    ALTER COLUMN enunciado DROP NOT NULL;
