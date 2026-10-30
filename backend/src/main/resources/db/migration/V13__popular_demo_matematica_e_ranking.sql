-- V13: dados demonstrativos materia + ranking

DO $$
DECLARE
    v_curso_id BIGINT;
    v_modulo_id BIGINT;
    v_aula_id BIGINT;
    v_questao_id BIGINT;
    v_professor_id BIGINT;
    v_turma_id BIGINT;
    alternativa RECORD;
    aulas_por_modulo TEXT[][] := ARRAY[
        ARRAY[
            'Equações: isolando a incógnita',
            'Equações com operações inversas',
            'Equações com parênteses',
            'Equações com frações',
            'Problemas com equações'
        ],
        ARRAY[
            'Função afim: conceitos básicos',
            'Coeficientes e representação',
            'Raiz e interceptos',
            'Crescimento e decrescimento',
            'Aplicações da função afim'
        ]
    ];
    descricoes_modulo TEXT[] := ARRAY[
        'Equações de primeiro grau, operações inversas e modelagem de problemas.',
        'Funções afins, gráficos, raízes e aplicações em situações do cotidiano.'
    ];
    v_questoes TEXT[][] := ARRAY[
        ARRAY[
            'Resolva 2x + 4 = 12.',
            'Qual é o valor de x em x - 7 = 5?',
            'Na equação 3x = 21, qual é o valor de x?',
            'Resolva 5 + x = 13.',
            'Se x + 9 = 20, então x é igual a:'
        ],
        ARRAY[
            'Resolva 4x - 8 = 16.',
            'Qual operação desfaz uma multiplicação por 6?',
            'Na equação x/3 = 7, qual é o valor de x?',
            'Resolva 2x + 10 = 30.',
            'Se 7x = 56, qual é o valor de x?'
        ],
        ARRAY[
            'Resolva 2(x + 3) = 14.',
            'Qual é a solução de 3(x - 2) = 15?',
            'Em 5(x + 1) = 30, qual é o valor de x?',
            'Resolva 4(x - 2) + 3 = 15.',
            'Qual é a solução de 2(3x + 1) = 20?'
        ],
        ARRAY[
            'Resolva x/2 + 3 = 7.',
            'Qual é o valor de x em (x + 4)/3 = 6?',
            'Resolva 2x/5 = 8.',
            'Na equação x/4 - 2 = 3, qual é x?',
            'Resolva (2x + 6)/4 = 5.'
        ],
        ARRAY[
            'Um número somado a 12 resulta em 25. Qual é o número?',
            'O dobro de um número é 18. Qual é o número?',
            'A terça parte de um número é 9. Qual é o número?',
            'João tinha x reais e recebeu 15, ficando com 42. Quanto tinha?',
            'Um retângulo tem perímetro 30 e lados x e 5. Qual é x?'
        ],
        ARRAY[
            'Na função f(x) = 2x + 3, qual é f(4)?',
            'Qual expressão representa uma função afim?',
            'Se f(x) = x - 5, qual é f(8)?',
            'Na função f(x) = 4x - 1, qual é o coeficiente angular?',
            'Qual é o valor de f(0) em f(x) = 7x + 2?'
        ],
        ARRAY[
            'Na função f(x) = 3x + 1, quais são os coeficientes a e b?',
            'O que representa o coeficiente b em f(x) = ax + b?',
            'Qual função tem coeficiente angular -2?',
            'Se a = 5 e b = -3, qual é a função afim?',
            'Em f(x) = -x + 6, qual é o coeficiente linear?'
        ],
        ARRAY[
            'Qual é a raiz de f(x) = 2x - 10?',
            'A raiz de f(x) = x + 4 é:',
            'Onde o gráfico de uma função afim cruza o eixo y?',
            'Qual é a raiz de f(x) = -3x + 12?',
            'A função f(x) = 5x possui qual raiz?'
        ],
        ARRAY[
            'A função f(x) = 3x + 2 é crescente ou decrescente?',
            'Uma função com coeficiente angular negativo é:',
            'Como varia f(x) = -4x + 1 quando x aumenta?',
            'Qual função é crescente?',
            'Se o coeficiente angular é zero, a função é:'
        ],
        ARRAY[
            'Um táxi cobra 8 reais de bandeirada e 3 por quilômetro. Qual função representa o custo?',
            'Uma corrida de 5 km no modelo C(x) = 6 + 2x custa quanto?',
            'Uma tarifa C(x) = 10 + 4x para 3 unidades resulta em:',
            'Se uma conta é P(x) = 20 + 5x, qual é o custo fixo?',
            'Uma vela mede 20 cm e diminui 2 cm por hora. Quanto mede após 6 horas?'
        ]
    ];
    v_alternativas TEXT[][] := ARRAY[
        ARRAY['x = 3','x = 4','x = 6','x = 8'],
        ARRAY['x = 10','x = 12','x = 5','x = 2'],
        ARRAY['x = 6','x = 7','x = 18','x = 24'],
        ARRAY['x = 5','x = 8','x = 13','x = 18'],
        ARRAY['x = 9','x = 11','x = 20','x = 29'],
        ARRAY['x = 2','x = 4','x = 6','x = 8'],
        ARRAY['Somar 6','Subtrair 6','Dividir por 6','Multiplicar por 6'],
        ARRAY['7','14','21','28'],
        ARRAY['x = 5','x = 10','x = 15','x = 20'],
        ARRAY['x = 6','x = 7','x = 8','x = 9'],
        ARRAY['x = 2','x = 4','x = 7','x = 10'],
        ARRAY['x = 3','x = 5','x = 7','x = 9'],
        ARRAY['x = 4','x = 5','x = 6','x = 7'],
        ARRAY['x = 2','x = 3','x = 4','x = 5'],
        ARRAY['x = 2','x = 3','x = 4','x = 5'],
        ARRAY['7','8','11','12'],
        ARRAY['ax + b','ax² + b','a/x + b','a + b'],
        ARRAY['3','5','8','13'],
        ARRAY['2','3','4','7'],
        ARRAY['2','3','4','-1'],
        ARRAY['a = 1, b = 3','a = 3, b = 1','a = 3, b = 0','a = 0, b = 3'],
        ARRAY['O valor inicial','A inclinação','A raiz','O domínio'],
        ARRAY['f(x) = 2x + 1','f(x) = -2x + 1','f(x) = x/2 + 1','f(x) = 2 + 1'],
        ARRAY['f(x) = 5x - 3','f(x) = 3x - 5','f(x) = 5 - 3x','f(x) = x + 5'],
        ARRAY['-6','-1','1','6'],
        ARRAY['x = -5','x = -4','x = 4','x = 5'],
        ARRAY['No eixo x','No eixo y','Na origem sempre','Fora do plano'],
        ARRAY['x = -4','x = 0','x = 3','x = 4'],
        ARRAY['0','1','5','10'],
        ARRAY['Crescente','Decrescente','Constante','Inexistente'],
        ARRAY['Sempre crescente','Sempre decrescente','Constante','Quadrática'],
        ARRAY['Aumenta','Diminui','Fica igual','Zera sempre'],
        ARRAY['f(x) = 2x + 1','f(x) = -2x + 1','f(x) = 1 - 2x','f(x) = 2'],
        ARRAY['Crescente','Decrescente','Constante','Indefinida'],
        ARRAY['C(x) = 8 + 3x','C(x) = 3 + 8x','C(x) = 8x - 3','C(x) = 3x - 8'],
        ARRAY['12','16','18','21'],
        ARRAY['18','20','22','24'],
        ARRAY['5','10','20','25'],
        ARRAY['6 cm','8 cm','12 cm','14 cm'],
        ARRAY['Crescente','Decrescente','Constante','Não é função'],
        ARRAY['Crescente','Decrescente','Constante','Indefinida'],
        ARRAY['Aumenta','Diminui','Fica igual','Não pode variar'],
        ARRAY['f(x) = 2x + 1','f(x) = -2x + 1','f(x) = 2','f(x) = 1'],
        ARRAY['Crescente','Decrescente','Constante','Quadrática'],
        ARRAY['8 + 3x','3 + 8x','8x - 3','3x - 8'],
        ARRAY['12','16','18','21'],
        ARRAY['18','20','22','24'],
        ARRAY['5','10','20','25'],
        ARRAY['6 cm','8 cm','12 cm','14 cm'],
        ARRAY['6 cm','8 cm','12 cm','14 cm']
    ];
    v_corretas INT[] := ARRAY[
        2,3,2,2,2, 4,3,3,2,3, 3,2,3,3,2,
        1,1,3,2,4, 2,1,2,1,2, 3,2,2,4,1,
        1,2,2,1,2, 1,2,2,3,1, 1,1,2,1,3, 1,2,3,3,1
    ];
    questao_index INT := 0;
    modulo_ordem INT;
    aula_ordem INT;
    questao_ordem INT;
BEGIN
    INSERT INTO materias (titulo, descricao, icone, cor, ativo, ordem)
    SELECT 'Matemática', 'Fundamentos de álgebra e funções para o Ensino Médio.',
           'calculate', 'primary', TRUE, 1
    WHERE NOT EXISTS (SELECT 1 FROM materias WHERE titulo = 'Matemática');

    SELECT id INTO v_curso_id
    FROM cursos
    WHERE nome = 'Matemática - Ensino Médio'
    LIMIT 1;

    IF v_curso_id IS NULL THEN
        INSERT INTO cursos (nome, descricao, icone)
        VALUES ('Matemática - Ensino Médio',
                'Trilha de reforço curricular focada na superação de dificuldades',
                'calculate')
        RETURNING id INTO v_curso_id;
    END IF;

    FOR modulo_ordem IN 1..2 LOOP
        SELECT id INTO v_modulo_id
        FROM modulos
        WHERE modulos.curso_id = v_curso_id AND modulos.ordem = modulo_ordem
        LIMIT 1;

        IF v_modulo_id IS NULL THEN
            INSERT INTO modulos (curso_id, nome, titulo, descricao, ordem, materia_id)
            SELECT v_curso_id, CASE WHEN modulo_ordem = 1 THEN 'Módulo 1: Equações de 1º Grau'
                                  ELSE 'Módulo 2: Funções de 1º Grau' END,
                   CASE WHEN modulo_ordem = 1 THEN 'Módulo 1: Equações de 1º Grau'
                                  ELSE 'Módulo 2: Funções de 1º Grau' END,
                   descricoes_modulo[modulo_ordem], modulo_ordem,
                   (SELECT id FROM materias WHERE titulo = 'Matemática' LIMIT 1)
            RETURNING id INTO v_modulo_id;
        END IF;

        FOR aula_ordem IN 1..5 LOOP
            SELECT id INTO v_aula_id
            FROM aulas
            WHERE aulas.modulo_id = v_modulo_id AND aulas.ordem = aula_ordem
            LIMIT 1;

            IF v_aula_id IS NULL THEN
                INSERT INTO aulas (modulo_id, nome, titulo, descricao, ordem, duracao_minutos, recompensa)
                VALUES (v_modulo_id, aulas_por_modulo[modulo_ordem][aula_ordem],
                        aulas_por_modulo[modulo_ordem][aula_ordem],
                        'Aula demonstrativa de Matemática.', aula_ordem, 20, 50)
                RETURNING id INTO v_aula_id;
            END IF;

            FOR questao_ordem IN 1..5 LOOP
                questao_index := questao_index + 1;
                INSERT INTO questoes (aula_id, curso_id, ordem, cabecalho, explicacao, dificuldade, xp_recompensa)
                VALUES (v_aula_id, v_curso_id, questao_ordem,
                        v_questoes[(modulo_ordem - 1) * 5 + aula_ordem][questao_ordem],
                        'Resolva passo a passo, aplicando as operações inversas e conferindo o resultado.',
                        CASE WHEN questao_ordem >= 4 THEN 'MEDIO' ELSE 'FACIL' END, 10)
                ON CONFLICT (aula_id, ordem) DO UPDATE
                    SET cabecalho = EXCLUDED.cabecalho,
                        curso_id = EXCLUDED.curso_id,
                        dificuldade = EXCLUDED.dificuldade,
                        xp_recompensa = EXCLUDED.xp_recompensa
                RETURNING id INTO v_questao_id;

                DELETE FROM alternativas WHERE alternativas.questao_id = v_questao_id;
                FOR alternativa IN
                    SELECT texto, ordem
                    FROM (VALUES
                        (v_alternativas[questao_index][1], 1),
                        (v_alternativas[questao_index][2], 2),
                        (v_alternativas[questao_index][3], 3),
                        (v_alternativas[questao_index][4], 4)
                    ) AS opcao(texto, ordem)
                LOOP
                    INSERT INTO alternativas (questao_id, texto, justificativa, correta)
                    VALUES (v_questao_id, alternativa.texto, 'Revise o conceito e tente novamente.',
                            alternativa.ordem = v_corretas[questao_index]);
                END LOOP;
            END LOOP;
        END LOOP;
    END LOOP;

    INSERT INTO usuarios (nome, username, email, senha, perfil, titulo_honra, xp, nivel, vidas, moedas, streak, best_streak)
    VALUES ('Prof. Ricardo Oliveira', 'prof.ricardo', 'prof.ricardo@escola.edu.br',
            '$2a$10$pC.sJBxBSGiLG52DpY8xIeSY.Ffimx/lQEp9uc6fB.JazD6D9eMfy',
            'PROFESSOR', 'Professor de Matemática', 0, 1, 3, 0, 0, 0)
    ON CONFLICT (email) DO UPDATE SET nome = EXCLUDED.nome
    RETURNING id INTO v_professor_id;
    IF v_professor_id IS NULL THEN
        SELECT id INTO v_professor_id FROM usuarios WHERE email = 'prof.ricardo@escola.edu.br';
    END IF;

    INSERT INTO usuarios (nome, username, email, senha, perfil, professor_id, titulo_honra, xp, nivel, vidas, moedas, streak, best_streak)
    VALUES
        ('Ana Beatriz', 'ana.beatriz', 'ana.beatriz@escola.edu.br', '$2a$10$pC.sJBxBSGiLG52DpY8xIeSY.Ffimx/lQEp9uc6fB.JazD6D9eMfy', 'ALUNO', v_professor_id, 'Aluna Destaque', 1500, 4, 3, 220, 8, 12),
        ('João Silva', 'joao.silva', 'joao.silva@escola.edu.br', '$2a$10$pC.sJBxBSGiLG52DpY8xIeSY.Ffimx/lQEp9uc6fB.JazD6D9eMfy', 'ALUNO', v_professor_id, 'Estudante Dedicado', 1200, 3, 3, 180, 7, 10),
        ('Lucas Mendes', 'lucas.mendes', 'lucas.mendes@escola.edu.br', '$2a$10$pC.sJBxBSGiLG52DpY8xIeSY.Ffimx/lQEp9uc6fB.JazD6D9eMfy', 'ALUNO', v_professor_id, 'Estudante Dedicado', 1000, 3, 3, 140, 5, 6),
        ('Marina Costa', 'marina.costa', 'marina.costa@escola.edu.br', '$2a$10$pC.sJBxBSGiLG52DpY8xIeSY.Ffimx/lQEp9uc6fB.JazD6D9eMfy', 'ALUNO', v_professor_id, 'Estudante Dedicado', 850, 2, 3, 110, 4, 5),
        ('Pedro Alves', 'pedro.alves', 'pedro.alves@escola.edu.br', '$2a$10$pC.sJBxBSGiLG52DpY8xIeSY.Ffimx/lQEp9uc6fB.JazD6D9eMfy', 'ALUNO', v_professor_id, 'Estudante Dedicado', 700, 2, 3, 90, 3, 4)
    ON CONFLICT (email) DO UPDATE
        SET xp = EXCLUDED.xp, nivel = EXCLUDED.nivel, professor_id = EXCLUDED.professor_id;

    SELECT id INTO v_turma_id FROM turmas WHERE nome = 'Matemática - 1º Ano' AND ano_letivo = 2026 LIMIT 1;
    IF v_turma_id IS NULL THEN
        INSERT INTO turmas (nome, ano_letivo, ativo)
        VALUES ('Matemática - 1º Ano', 2026, TRUE)
        RETURNING id INTO v_turma_id;
    END IF;

    INSERT INTO turma_alunos (turma_id, usuario_id)
    SELECT v_turma_id, id FROM usuarios
    WHERE email IN ('ana.beatriz@escola.edu.br', 'joao.silva@escola.edu.br',
                    'lucas.mendes@escola.edu.br', 'marina.costa@escola.edu.br',
                    'pedro.alves@escola.edu.br')
    ON CONFLICT (turma_id, usuario_id) DO NOTHING;
END $$;
