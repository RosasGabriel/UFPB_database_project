-- Caso todos os IDs existam (Roda com sucesso):
INSERT INTO ATENDIMENTO (data_hora, duracao_minutos, id_paciente, id_residente, id_preceptor, id_unidade)
VALUES ('2026-07-13 14:00:00', 40, 1, 6, 11, 2);

-- Exemplo de teste para falhar (id_paciente 99 não existe):
-- INSERT INTO ATENDIMENTO (data_hora, duracao_minutos, id_paciente, id_residente, id_preceptor, id_unidade)
-- VALUES ('2026-07-13 14:00:00', 40, 99, 6, 11, 2);

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

SELECT 
    pr.id_atendimento,
    proc.nome AS nome_procedimento,
    pr.quantidade,
    pr.tempo_real_minutos
FROM PROCEDIMENTO_REALIZADO pr
JOIN PROCEDIMENTO proc ON pr.id_procedimento = proc.id_procedimento
WHERE pr.id_atendimento = 3;

UPDATE PACIENTE
SET num_convenio = 'GEAP-55443'
WHERE id_pessoa = 4;

-- Para conferir a alteração:
-- SELECT * FROM PACIENTE WHERE id_pessoa = 4;

-- Tentativa 1: Tentar deletar um registro faturado (id_atendimento = 5, id_procedimento = 3 -> pode_remover é FALSE). 
-- O banco não vai deletar nenhuma linha.
DELETE FROM PROCEDIMENTO_REALIZADO
WHERE id_atendimento = 5 AND id_procedimento = 3 AND pode_remover = TRUE;

-- Tentativa 2: Tentar deletar um registro NÃO faturado (id_atendimento = 1, id_procedimento = 1 -> pode_remover é TRUE).
-- Esse vai deletar com sucesso.
DELETE FROM PROCEDIMENTO_REALIZADO
WHERE id_atendimento = 1 AND id_procedimento = 1 AND pode_remover = TRUE;

SELECT 
    r.id_pessoa AS id_residente,
    p.nome AS nome_residente,
    ROUND(AVG(a.duracao_minutos), 2) AS tempo_medio_atendimento_minutos
FROM ATENDIMENTO a
JOIN RESIDENTE r ON a.id_residente = r.id_pessoa
JOIN PESSOA p ON r.id_pessoa = p.id_pessoa
GROUP BY r.id_pessoa, p.nome;