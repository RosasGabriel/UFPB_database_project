-- =============================================================================
-- 1. INSERÇÃO NA TABELA PESSOA (Base para todos os envolvidos)
-- =============================================================================
INSERT INTO PESSOA (nome, CPF, data_nascimento, is_flamengo, telefone) VALUES
-- Pacientes
('Arthur Antunes Coimbra', '11122233344', '1953-03-03', TRUE, '83988881111'),   -- Paciente 1
('Maria Souza Oliveira',   '22233344455', '1995-08-15', FALSE, '83988882222'),  -- Paciente 2
('Bruno Henrique Pinto',   '33344455566', '1990-12-30', TRUE, '83988883333'),   -- Paciente 3
('Ana Clara Lima',         '44455566677', '2001-04-12', FALSE, '83988884444'),  -- Paciente 4
('Gabigol Silva',          '55566677788', '1996-08-30', TRUE, '83988885555'),   -- Paciente 5
-- Residentes
('Roberto Carlos Silva',   '66677788899', '1998-05-20', FALSE, '83999991111'),  -- Residente 1 (id_pessoa = 6)
('Mariana Costa Lima',     '77788899900', '1999-11-02', TRUE, '83999992222'),   -- Residente 2 (id_pessoa = 7)
('Carlos Eduardo Souza',   '88899900011', '1997-01-15', FALSE, '83999993333'),  -- Residente 3 (id_pessoa = 8)
('Juliana Alves Pereira',  '99900011122', '1996-07-24', TRUE, '83999994444'),   -- Residente 4 (id_pessoa = 9)
('Lucas Santos Oliveira',  '00011122233', '1998-03-09', FALSE, '83999995555'),  -- Residente 5 (id_pessoa = 10)
-- Preceptores
('Dr. Arnaldo Cezar',      '12345678901', '1970-01-15', TRUE, '83977771111'),   -- Preceptor 1 (id_pessoa = 11)
('Dra. Sandra Regina',     '23456789012', '1975-06-22', FALSE, '83977772222'),  -- Preceptor 2 (id_pessoa = 12)
('Dr. Moacir Santos',      '34567890123', '1968-09-05', FALSE, '83977773333'),  -- Preceptor 3 (id_pessoa = 13)
('Dra. Patricia Pillar',   '45678901234', '1980-03-18', TRUE, '83977774444'),   -- Preceptor 4 (id_pessoa = 14)
('Dr. Reynaldo Gianec',    '56789012345', '1978-11-12', FALSE, '83977775555');  -- Preceptor 5 (id_pessoa = 15)

-- =============================================================================
-- 2. INSERÇÃO NA TABELA PACIENTE
-- =============================================================================
INSERT INTO PACIENTE (id_pessoa, num_convenio, alergias, grupo_sanguineo) VALUES
(1, 'UNIMED-12345', 'Nenhuma', 'O+'),
(2, 'CASSI-98765', 'Dipirona', 'A-'),
(3, 'SULAMERICA-11', 'Penicilina', 'AB+'),
(4, NULL, 'Lactose', 'B+'), -- SUS / Sem convênio
(5, 'AMIL-77777', 'Nenhuma', 'O-');

-- =============================================================================
-- 3. INSERÇÃO NA TABELA PROFISSIONAL (Base comum para médicos)
-- =============================================================================
INSERT INTO PROFISSIONAL (id_pessoa, CRM, data_admissao, especialidade) VALUES
-- Residentes
(6,  'CRM-PB-6001', '2025-03-01', 'Pediatria'),
(7,  'CRM-PB-6002', '2025-03-01', 'Clínica Médica'),
(8,  'CRM-PB-6003', '2025-03-01', 'Cardiologia'),
(9,  'CRM-PB-6004', '2026-03-01', 'Ginecologia'),
(10, 'CRM-PB-6005', '2026-03-01', 'Clínica Médica'),
-- Preceptores
(11, 'CRM-PB-1001', '2010-02-10', 'Pediatria'),
(12, 'CRM-PB-1002', '2012-05-15', 'Clínica Médica'),
(13, 'CRM-PB-1003', '2008-11-01', 'Cardiologia'),
(14, 'CRM-PB-1004', '2015-08-20', 'Ginecologia'),
(15, 'CRM-PB-1005', '2014-04-01', 'Clínica Médica');

-- =============================================================================
-- 4. INSERÇÃO NA TABELA PRECEPTOR
-- =============================================================================
INSERT INTO PRECEPTOR (id_pessoa, titulacao) VALUES
(11, 'Doutorado'),
(12, 'Mestrado'),
(13, 'Doutorado'),
(14, 'Mestrado'),
(15, 'Especialização');

-- =============================================================================
-- 5. INSERÇÃO NA TABELA RESIDENTE
-- =============================================================================
INSERT INTO RESIDENTE (id_pessoa, ano_residencia) VALUES
(6,  1),
(7,  1),
(8,  1),
(9,  2),
(10, 2);

-- =============================================================================
-- 6. INSERÇÃO NA TABELA UNIDADE (3 Unidades)
-- =============================================================================
INSERT INTO UNIDADE (nome, tipo, capacidade_leitos) VALUES
('UTI Geral', 'Terapia Intensiva', 15),
('Emergência Adulto', 'Pronto Atendimento', 30),
('Enfermaria Pediátrica', 'Internação', 20);

-- =============================================================================
-- 7. INSERÇÃO NA TABELA PROCEDIMENTO
-- =============================================================================
INSERT INTO PROCEDIMENTO (nome, tempo_medio_execucao) VALUES
('Sutura Simples', 20),
('Acesso Venoso Central', 40),
('Intubação Orotraqueal', 15),
('Curativo Complexo', 30),
('Eletrocardiograma (ECG)', 10);

-- =============================================================================
-- 8. INSERÇÃO NA TABELA ATENDIMENTO (10 Atendimentos)
-- =============================================================================
-- Relações consistentes com especialidades
INSERT INTO ATENDIMENTO (id_atendimento, data_hora, duracao_minutos, id_paciente, id_residente, id_preceptor, id_unidade) VALUES
(1,  '2026-07-01 08:30:00', 45, 1, 6,  11, 3), -- Paciente 1, Residente Pediatria, Preceptor Pediatria
(2,  '2026-07-02 10:15:00', 30, 2, 7,  12, 2), -- Paciente 2, Residente Clínica, Preceptor Clínica
(3,  '2026-07-03 14:00:00', 60, 3, 8,  13, 1), -- Paciente 3, Residente Cardio, Preceptor Cardio
(4,  '2026-07-04 19:30:00', 50, 4, 9,  14, 2), -- Paciente 4, Residente Gineco, Preceptor Gineco
(5,  '2026-07-05 22:00:00', 40, 5, 10, 15, 2), -- Paciente 5, Residente Clínica, Preceptor Clínica
(6,  '2026-07-06 09:00:00', 35, 1, 6,  11, 3), -- Paciente 1 novamente
(7,  '2026-07-07 11:30:00', 25, 2, 7,  12, 2), -- Paciente 2 novamente
(8,  '2026-07-08 16:45:00', 90, 3, 8,  13, 1), -- Paciente 3 novamente
(9,  '2026-07-09 21:15:00', 45, 4, 9,  14, 2), -- Paciente 4 novamente
(10, '2026-07-10 07:45:00', 55, 5, 10, 15, 2); -- Paciente 5 novamente
(11, '2026-07-11 08:00:00', 40, 1, 6,  11, 3), -- Dr. Arnaldo (Supervisão 3)
(12, '2026-07-11 10:30:00', 50, 2, 6,  11, 3), -- Dr. Arnaldo (Supervisão 4)
(13, '2026-07-12 14:15:00', 30, 3, 7,  11, 2), -- Dr. Arnaldo (Supervisão 5)
(14, '2026-07-12 16:00:00', 45, 4, 7,  11, 2), -- Dr. Arnaldo (Supervisão 6)
(15, '2026-07-13 09:00:00', 60, 5, 10, 11, 2), -- Dr. Arnaldo (Supervisão 7)
(16, '2026-07-13 11:30:00', 35, 1, 7,  12, 2), -- Dra. Sandra (Supervisão 3)
(17, '2026-07-14 08:15:00', 25, 2, 7,  12, 2), -- Dra. Sandra (Supervisão 4)
(18, '2026-07-14 13:00:00', 40, 3, 10, 12, 2), -- Dra. Sandra (Supervisão 5)
(19, '2026-07-14 15:45:00', 55, 4, 10, 12, 2), -- Dra. Sandra (Supervisão 6)
(20, '2026-07-15 10:00:00', 70, 5, 8,  13, 1),
(21, '2026-07-15 14:30:00', 30, 1, 8,  13, 1),
(22, '2026-07-16 09:15:00', 20, 2, 9,  14, 2),
(23, '2026-07-16 11:00:00', 45, 3, 9,  14, 2),
(24, '2026-07-17 16:00:00', 35, 4, 10, 15, 2),
(25, '2026-07-17 19:30:00', 50, 5, 6,  11, 3), -- Dr. Arnaldo (Supervisão 8)
(26, '2026-07-18 08:00:00', 40, 1, 7,  12, 2), -- Dra. Sandra (Supervisão 7)
(27, '2026-07-18 10:30:00', 60, 2, 8,  13, 1),
(28, '2026-07-19 14:00:00', 90, 3, 9,  14, 2),
(29, '2026-07-19 22:00:00', 40, 4, 10, 15, 2),
(30, '2026-07-20 07:30:00', 30, 5, 6,  11, 3); -- Dr. Arnaldo (Supervisão 9)

-- Ajustando a sequência de IDs gerada para a tabela Atendimento no PostgreSQL
SELECT setval('atendimento_id_atendimento_seq', 30);

-- =============================================================================
-- 9. INSERÇÃO NA TABELA PROCEDIMENTO_REALIZADO (10 Procedimentos) - CORRIGIDO
-- =============================================================================
INSERT INTO PROCEDIMENTO_REALIZADO (id_atendimento, id_procedimento, quantidade, tempo_real_minutos, observacao, pode_remover) VALUES
(1,  1, 1, 25, 'Sutura simples no braço esquerdo da criança.', TRUE),       -- Atendimento 1 realizou Procedimento 1 (Sutura)
(2,  5, 1, 10, 'Eletrocardiograma de rotina para queixa de palpitação.', TRUE), -- Atendimento 2 realizou Procedimento 5 (ECG)
(3,  2, 2, 35, 'Dois acessos venosos periféricos estabelecidos sem intercorrências.', TRUE), -- Atendimento 3 realizou Procedimento 2 (Acesso)
(4,  3, 3, 20, 'Eletrocardiogramas seriados.', TRUE),                      -- Atendimento 4 realizou Procedimento 3 (Intubação) 
(5,  3, 2, 45, 'Acesso venoso central guiado por ultrassom.', FALSE),       -- Atendimento 5 realizou Procedimento 3 (Intubação)
(6,  1, 1, 15, 'Sutura na face, paciente colaborativo.', TRUE),            -- Atendimento 6 realizou Procedimento 1 (Sutura)
(7,  3, 1, 20, 'Intubação realizada sob supervisão direta.', FALSE),        -- Atendimento 7 realizou Procedimento 3 (Intubação)
(8,  4, 1, 35, 'Curativo pós-cirúrgico de grande porte.', TRUE),            -- Atendimento 8 realizou Procedimento 4 (Curativo)
(9,  5, 1, 15, 'ECG rápido para verificação.', TRUE),                      -- Atendimento 9 realizou Procedimento 5 (ECG)
(10, 1, 1, 25, 'Sutura em região plantar.', TRUE);                         -- Atendimento 10 realizou Procedimento 1 (Sutura)
(11, 1, 1, 35, 'Sutura simples sob supervisão.', TRUE),
(12, 5, 1, 12, 'ECG de controle.', TRUE),
(13, 2, 1, 40, 'Acesso venoso central.', TRUE),
(14, 4, 1, 30, 'Curativo pós-cirúrgico.', TRUE),
(15, 3, 1, 15, 'Intubação rápida de emergência.', FALSE), -- Faturado
(16, 1, 2, 40, 'Duas suturas pequenas.', TRUE),
(17, 5, 1, 10, 'ECG de rotina.', TRUE),
(18, 2, 1, 35, 'Acesso venoso estabelecido.', TRUE),
(19, 4, 1, 25, 'Curativo simples.', TRUE),
(20, 3, 1, 18, 'Intubação em leito de UTI.', FALSE), -- Faturado
(21, 5, 1, 11, 'ECG de controle.', TRUE),
(22, 1, 1, 20, 'Sutura em dedo da mão.', TRUE),
(23, 4, 1, 35, 'Troca de curativo complexo.', TRUE),
(24, 2, 1, 45, 'Acesso central guiado.', TRUE),
(25, 5, 1, 15, 'ECG para paciente cardiopata.', TRUE),
(26, 1, 1, 25, 'Sutura de ferimento cortante.', TRUE),
(27, 3, 1, 14, 'Intubação sob supervisão.', FALSE), -- Faturado
(28, 2, 1, 38, 'Acesso venoso difícil.', TRUE),
(29, 4, 1, 30, 'Curativo padrão.', TRUE),
(30, 5, 1, 10, 'ECG rápido.', TRUE);
-- =============================================================================
-- 10. INSERÇÃO NA TABELA ESCALA (Para deixar o banco completo e funcional)
-- =============================================================================
INSERT INTO ESCALA (id_unidade, dia_semana, turno, id_residente, id_preceptor) VALUES
(2, 'Segunda', 'Manhã', 7, 12),
(1, 'Quarta', 'Noite', 8, 13),
(3, 'Sexta', 'Tarde', 6, 11);