-- =============================================================================
-- PARTE 1: OPERAÇÕES DE CRUD
-- =============================================================================

-- 1.1 INSERÇÃO: Registro de novo atendimento (Sucesso)
-- Executa normalmente se todos os IDs referenciados existirem nas tabelas pai.
INSERT INTO ATENDIMENTO (data_hora, duracao_minutos, id_paciente, id_residente, id_preceptor, id_unidade)
VALUES ('2026-07-13 14:00:00', 40, 1, 6, 11, 2);

-- 1.2 TESTE DE ERRO: Tentativa de inserção violando Chave Estrangeira (FK)
-- O banco deve rejeitar este comando porque o id_paciente 99 não existe na tabela PACIENTE.
-- INSERT INTO ATENDIMENTO (data_hora, duracao_minutos, id_paciente, id_residente, id_preceptor, id_unidade)
-- VALUES ('2026-07-13 14:00:00', 40, 99, 6, 11, 2);


-- 1.3 LEITURA: Histórico de consultas de um paciente específico (Filtro por ID)
-- Retorna os dados ordenados cronologicamente do paciente de ID 1.
SELECT 
    a.id_atendimento,
    a.data_hora,
    a.duracao_minutos,
    p_pac.nome AS nome_paciente,
    p_res.nome AS nome_residente,
    u.nome AS unidade_atendimento
FROM ATENDIMENTO a
JOIN PESSOA p_pac ON a.id_paciente = p_pac.id_pessoa
JOIN PESSOA p_res ON a.id_residente = p_res.id_pessoa
JOIN UNIDADE u ON a.id_unidade = u.id_unidade
WHERE a.id_paciente = 1
ORDER BY a.data_hora ASC;


-- 1.4 LEITURA: Procedimentos realizados em um Atendimento específico
-- Lista quais procedimentos foram conduzidos no atendimento de ID 3.
SELECT 
    pr.id_atendimento,
    proc.nome AS nome_procedimento,
    pr.quantidade,
    pr.tempo_real_minutos
FROM PROCEDIMENTO_REALIZADO pr
JOIN PROCEDIMENTO proc ON pr.id_procedimento = proc.id_procedimento
WHERE pr.id_atendimento = 3;


-- 1.5 ATUALIZAÇÃO (UPDATE): Atualizar dados cadastrais de um paciente
-- Modifica o número do convênio do paciente de ID 4.
UPDATE PACIENTE
SET num_convenio = 'GEAP-55443'
WHERE id_pessoa = 4;


-- 1.6 EXCLUSÃO CONDICIONAL (DELETE com Regra de Negócio de Faturamento):
-- Tentativa 1: Tentar deletar um registro faturado (pode_remover = FALSE). 
-- O WHERE com 'pode_remover = TRUE' impede que o registro seja excluído silenciosamente se já tiver sido faturado.
DELETE FROM PROCEDIMENTO_REALIZADO
WHERE id_atendimento = 5 AND id_procedimento = 3 AND pode_remover = TRUE;

-- Tentativa 2: Tentar deletar um registro NÃO faturado (pode_remover = TRUE).
-- O comando deve ser executado com sucesso e a linha removida do banco.
DELETE FROM PROCEDIMENTO_REALIZADO
WHERE id_atendimento = 1 AND id_procedimento = 1 AND pode_remover = TRUE;

-- Consulta os IDs reais de Residentes e Preceptores cadastrados (consulta adicionada para finalidade de testes)
SELECT p.id_pessoa, p.nome, 'RESIDENTE' AS tipo 
FROM RESIDENTE r JOIN PESSOA p ON r.id_pessoa = p.id_pessoa
UNION ALL
SELECT p.id_pessoa, p.nome, 'PRECEPTOR' AS tipo 
FROM PRECEPTOR prec JOIN PESSOA p ON prec.id_pessoa = p.id_pessoa;


-- =============================================================================
-- PARTE 2: CONSULTAS ANALÍTICAS
-- =============================================================================

-- 2.1 RELATÓRIO: Desempenho médio por médico Residente
-- Calcula o tempo médio (em minutos) gasto por cada residente em seus atendimentos.
SELECT 
    r.id_pessoa AS id_residente,
    p.nome AS nome_residente,
    ROUND(AVG(a.duracao_minutos), 2) AS tempo_medio_atendimento_minutos
FROM ATENDIMENTO a
JOIN RESIDENTE r ON a.id_residente = r.id_pessoa
JOIN PESSOA p ON r.id_pessoa = p.id_pessoa
GROUP BY r.id_pessoa, p.nome;


-- 2.2 RANKING: Produtividade dos Residentes (Função de Janela DENSE_RANK)
-- Classifica os residentes com base na quantidade total de atendimentos realizados.
SELECT 
    DENSE_RANK() OVER (ORDER BY COUNT(a.id_atendimento) DESC) AS posicao_ranking,
    p.nome AS nome_residente,
    COUNT(a.id_atendimento) AS total_atendimentos
FROM ATENDIMENTO a
JOIN RESIDENTE r ON a.id_residente = r.id_pessoa
LEFT JOIN PESSOA p ON r.id_pessoa = p.id_pessoa
GROUP BY r.id_pessoa, p.nome;


-- 2.3 RELATÓRIO DE SUPERVISÃO: Preceptores com alto volume de atendimentos
-- Filtra preceptores que supervisionaram estritamente mais de 5 atendimentos em Julho/2026.
-- Útil para identificar sobrecarga de trabalho ou distribuição de residentes.
SELECT 
    p.nome AS nome_preceptor,
    COUNT(a.id_atendimento) AS total_supervisoes
FROM ATENDIMENTO a
JOIN PRECEPTOR prec ON a.id_preceptor = prec.id_pessoa
LEFT JOIN PESSOA p ON prec.id_pessoa = p.id_pessoa
WHERE EXTRACT(MONTH FROM a.data_hora) = 7 
  AND EXTRACT(YEAR FROM a.data_hora) = 2026
GROUP BY prec.id_pessoa, p.nome
HAVING COUNT(a.id_atendimento) > 5;


-- 2.4 ESCALAS: Quantidade de plantões escalados por residente em cada setor
-- Consolida as escalas cadastradas, ordenando do residente mais escalado para o menos escalado por unidade.
SELECT 
    u.nome AS unidade,
    p.nome AS nome_residente,
    COUNT(e.id_escala) AS quantidade_plantoes
FROM ESCALA e
JOIN UNIDADE u ON e.id_unidade = u.id_unidade
LEFT JOIN RESIDENTE r ON e.id_residente = r.id_pessoa
LEFT JOIN PESSOA p ON r.id_pessoa = p.id_pessoa
GROUP BY u.nome, r.id_pessoa, p.nome
ORDER BY u.nome, quantidade_plantoes DESC;


-- 2.5 SEGURANÇA DO PACIENTE: Identificação de pacientes sem procedimentos invasivos/de alto risco
-- Seleciona pacientes que nunca passaram por Intubação Orotraqueal ou Acesso Venoso Central.
-- Utiliza subquery com NOT EXISTS para otimização de busca.
SELECT pac_p.nome AS nome_paciente
FROM PACIENTE pac
JOIN PESSOA pac_p ON pac.id_pessoa = pac_p.id_pessoa
WHERE NOT EXISTS (
    SELECT 1 
    FROM PROCEDIMENTO_REALIZADO pr
    JOIN PROCEDIMENTO proc ON pr.id_procedimento = proc.id_procedimento
    JOIN ATENDIMENTO at ON pr.id_atendimento = at.id_atendimento
    WHERE at.id_paciente = pac.id_pessoa 
      AND proc.nome IN ('Intubação Orotraqueal', 'Acesso Venoso Central') -- Procedimentos de alto risco definidos no escopo do projeto
);