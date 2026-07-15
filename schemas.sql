-- =============================================================================
-- 1. TABELAS FORTES (ENTIDADES INDEPENDENTES)
-- =============================================================================

-- Tabela PESSOA (Entidade base para a generalização/especialização)
CREATE TABLE PESSOA (
    id_pessoa SERIAL PRIMARY KEY, -- Chave Primária autoincrementada (PK)
    nome VARCHAR(100) NOT NULL,    -- Garantia de preenchimento obrigatório
    CPF VARCHAR(11) NOT NULL UNIQUE, -- CPF único no sistema (Constraint UNIQUE)
    data_nascimento DATE NOT NULL,
    is_flamengo BOOLEAN NOT NULL DEFAULT FALSE, -- Campo lógico com valor padrão padrão
    telefone VARCHAR(15),
    -- Constraint CHECK: Garante a consistência exata do tamanho do CPF
    CONSTRAINT chk_cpf_length CHECK (LENGTH(CPF) = 11)
);

-- Tabela UNIDADE (Setores e alas do hospital)
CREATE TABLE UNIDADE (
    id_unidade SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE, -- Nome do setor único (ex: 'UTI Geral')
    tipo VARCHAR(50) NOT NULL,
    -- Constraint CHECK: Impede valores negativos para a capacidade física
    capacidade_leitos INT NOT NULL CONSTRAINT chk_capacidade CHECK (capacidade_leitos >= 0)
);

-- Tabela PROCEDIMENTO (Catálogo geral de procedimentos do hospital)
CREATE TABLE PROCEDIMENTO (
    id_procedimento SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    -- Constraint CHECK: Tempo estimado deve ser obrigatoriamente positivo
    tempo_medio_execucao INT NOT NULL CONSTRAINT chk_tempo_medio CHECK (tempo_medio_execucao > 0)
);

-- =============================================================================
-- 2. ESPECIALIZAÇÕES / HERANÇAS (ENTIDADES DERIVADAS DE PESSOA)
-- =============================================================================

-- Tabela PACIENTE (Especialização de PESSOA)
CREATE TABLE PACIENTE (
    -- id_pessoa é Chave Primária e Estrangeira simultaneamente (Herança 1:1)
    id_pessoa INT PRIMARY KEY REFERENCES PESSOA(id_pessoa) ON DELETE CASCADE,
    num_convenio VARCHAR(50), -- Opcional (Permite NULL para simular atendimento SUS)
    alergias TEXT,
    -- Constraint CHECK: Restringe a entrada apenas para os tipos sanguíneos reais
    grupo_sanguineo VARCHAR(3) CONSTRAINT chk_grupo_sanguineo CHECK (grupo_sanguineo IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'))
);

-- Tabela PROFISSIONAL (Especialização de PESSOA - Base comum para médicos)
CREATE TABLE PROFISSIONAL (
    id_pessoa INT PRIMARY KEY REFERENCES PESSOA(id_pessoa) ON DELETE CASCADE,
    CRM VARCHAR(20) NOT NULL UNIQUE, -- Registro do conselho obrigatório e sem duplicidade
    data_admissao DATE NOT NULL,
    especialidade VARCHAR(100) NOT NULL
);

-- Tabela PRECEPTOR (Especialização de PROFISSIONAL)
CREATE TABLE PRECEPTOR (
    id_pessoa INT PRIMARY KEY REFERENCES PROFISSIONAL(id_pessoa) ON DELETE CASCADE,
    titulacao VARCHAR(50) NOT NULL -- Ex: Especialização, Mestrado, Doutorado
);

-- Tabela RESIDENTE (Especialização de PROFISSIONAL)
CREATE TABLE RESIDENTE (
    id_pessoa INT PRIMARY KEY REFERENCES PROFISSIONAL(id_pessoa) ON DELETE CASCADE,
    -- Constraint CHECK: Garante que o ano letivo da residência seja no mínimo 1
    ano_residencia INT NOT NULL CONSTRAINT chk_ano_residencia CHECK (ano_residencia >= 1)
);

-- =============================================================================
-- 3. TABELAS DE VÍNCULO, ASSOCIAÇÃO E AGENDAMENTO
-- =============================================================================

-- Tabela ATENDIMENTO (Registros dos atendimentos médicos realizados)
CREATE TABLE ATENDIMENTO (
    id_atendimento SERIAL PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data padrão automática
    -- Constraint CHECK: A duração da consulta precisa ser válida (maior que zero)
    duracao_minutos INT NOT NULL CONSTRAINT chk_duracao_atendimento CHECK (duracao_minutos > 0),
    
    -- Chaves Estrangeiras (FK) obrigatórias.
    -- ON DELETE RESTRICT: Impede a exclusão de pessoas ou unidades se houver histórico de atendimentos vinculados a eles.
    id_paciente INT NOT NULL REFERENCES PACIENTE(id_pessoa) ON DELETE RESTRICT,
    id_residente INT NOT NULL REFERENCES RESIDENTE(id_pessoa) ON DELETE RESTRICT,
    id_preceptor INT NOT NULL REFERENCES PRECEPTOR(id_pessoa) ON DELETE RESTRICT,
    id_unidade INT NOT NULL REFERENCES UNIDADE(id_unidade) ON DELETE RESTRICT
);

-- Tabela PROCEDIMENTO_REALIZADO (Relação de muitos-para-muitos entre Atendimento e Procedimento)
CREATE TABLE PROCEDIMENTO_REALIZADO (
    -- Chaves Estrangeiras referenciando as tabelas associadas.
    -- ON DELETE CASCADE: Se um atendimento for deletado, os registros de procedimentos dele caem juntos automaticamente.
    id_atendimento INT REFERENCES ATENDIMENTO(id_atendimento) ON DELETE CASCADE,
    id_procedimento INT REFERENCES PROCEDIMENTO(id_procedimento) ON DELETE RESTRICT,
    
    quantidade INT NOT NULL DEFAULT 1 CONSTRAINT chk_quantidade CHECK (quantidade > 0),
    tempo_real_minutos INT NOT NULL CONSTRAINT chk_tempo_real CHECK (tempo_real_minutos > 0),
    observacao TEXT,
    
    pode_remover BOOLEAN NOT NULL DEFAULT TRUE, -- Flag lógica usada para controle de faturamento
    
    PRIMARY KEY (id_atendimento, id_procedimento) -- Chave Primária Composta
);

-- Tabela ESCALA (Gestão de escalas de plantão de residentes e preceptores)
CREATE TABLE ESCALA (
    id_escala SERIAL PRIMARY KEY,
    id_unidade INT NOT NULL REFERENCES UNIDADE(id_unidade) ON DELETE CASCADE,
    
    -- Constraints CHECK: Validam domínios fixos para os dias e turnos possíveis
    dia_semana VARCHAR(15) NOT NULL CONSTRAINT chk_dia_semana CHECK (dia_semana IN ('Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo')),
    turno VARCHAR(15) NOT NULL CONSTRAINT chk_turno CHECK (turno IN ('Manhã', 'Tarde', 'Noite', 'Plantão 12h', 'Plantão 24h')),
    
    id_residente INT NOT NULL REFERENCES RESIDENTE(id_pessoa) ON DELETE CASCADE,
    id_preceptor INT NOT NULL REFERENCES PRECEPTOR(id_pessoa) ON DELETE CASCADE,
    
    -- Constraint UNIQUE Composta: Evita choque de horários (Residente alocado duas vezes na mesma unidade, dia e turno)
    CONSTRAINT uq_escala_residente UNIQUE (id_unidade, dia_semana, turno, id_residente)
);