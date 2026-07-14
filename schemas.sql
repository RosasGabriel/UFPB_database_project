-- 1. Tabela PESSOA (Base para a herança/especialização)
CREATE TABLE PESSOA (
    id_pessoa SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    CPF VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    is_flamengo BOOLEAN NOT NULL DEFAULT FALSE,
    telefone VARCHAR(15),
    CONSTRAINT chk_cpf_length CHECK (LENGTH(CPF) = 11)
);

-- 2. Tabela UNIDADE
CREATE TABLE UNIDADE (
    id_unidade SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    tipo VARCHAR(50) NOT NULL,
    capacidade_leitos INT NOT NULL CONSTRAINT chk_capacidade CHECK (capacidade_leitos >= 0)
);

-- 3. Tabela PROCEDIMENTO
CREATE TABLE PROCEDIMENTO (
    id_procedimento SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    tempo_medio_execucao INT NOT NULL CONSTRAINT chk_tempo_medio CHECK (tempo_medio_execucao > 0)
);

-- 4. Tabela PACIENTE
CREATE TABLE PACIENTE (
    id_pessoa INT PRIMARY KEY REFERENCES PESSOA(id_pessoa) ON DELETE CASCADE,
    num_convenio VARCHAR(50),
    alergias TEXT,
    grupo_sanguineo VARCHAR(3) CONSTRAINT chk_grupo_sanguineo CHECK (grupo_sanguineo IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'))
);

-- 5. Tabela PROFISSIONAL
CREATE TABLE PROFISSIONAL (
    id_pessoa INT PRIMARY KEY REFERENCES PESSOA(id_pessoa) ON DELETE CASCADE,
    CRM VARCHAR(20) NOT NULL UNIQUE,
    data_admissao DATE NOT NULL,
    especialidade VARCHAR(100) NOT NULL
);

-- 6. Tabela PRECEPTOR
CREATE TABLE PRECEPTOR (
    id_pessoa INT PRIMARY KEY REFERENCES PROFISSIONAL(id_pessoa) ON DELETE CASCADE,
    titulacao VARCHAR(50) NOT NULL
);

-- 7. Tabela RESIDENTE
CREATE TABLE RESIDENTE (
    id_pessoa INT PRIMARY KEY REFERENCES PROFISSIONAL(id_pessoa) ON DELETE CASCADE,
    ano_residencia INT NOT NULL CONSTRAINT chk_ano_residencia CHECK (ano_residencia >= 1)
);

-- 8. Tabela ATENDIMENTO
CREATE TABLE ATENDIMENTO (
    id_atendimento SERIAL PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duracao_minutos INT NOT NULL CONSTRAINT chk_duracao_atendimento CHECK (duracao_minutos > 0),
    id_paciente INT NOT NULL REFERENCES PACIENTE(id_pessoa) ON DELETE RESTRICT,
    id_residente INT NOT NULL REFERENCES RESIDENTE(id_pessoa) ON DELETE RESTRICT,
    id_preceptor INT NOT NULL REFERENCES PRECEPTOR(id_pessoa) ON DELETE RESTRICT,
    id_unidade INT NOT NULL REFERENCES UNIDADE(id_unidade) ON DELETE RESTRICT
);

-- 9. Tabela PROCEDIMENTO_REALIZADO (Tabela associativa entre Atendimento e Procedimento)
CREATE TABLE PROCEDIMENTO_REALIZADO (
    id_atendimento INT REFERENCES ATENDIMENTO(id_atendimento) ON DELETE CASCADE,
    id_procedimento INT REFERENCES PROCEDIMENTO(id_procedimento) ON DELETE RESTRICT,
    quantidade INT NOT NULL DEFAULT 1 CONSTRAINT chk_quantidade CHECK (quantidade > 0),
    tempo_real_minutos INT NOT NULL CONSTRAINT chk_tempo_real CHECK (tempo_real_minutos > 0),
    observacao TEXT,
    pode_remover BOOLEAN NOT NULL DEFAULT TRUE, -- Flag de controle solicitada no CRUD para faturamento
    PRIMARY KEY (id_atendimento, id_procedimento) -- Chave primária composta
);

-- 10. Tabela ESCALA
CREATE TABLE ESCALA (
    id_escala SERIAL PRIMARY KEY,
    id_unidade INT NOT NULL REFERENCES UNIDADE(id_unidade) ON DELETE CASCADE,
    dia_semana VARCHAR(15) NOT NULL CONSTRAINT chk_dia_semana CHECK (dia_semana IN ('Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo')),
    turno VARCHAR(15) NOT NULL CONSTRAINT chk_turno CHECK (turno IN ('Manhã', 'Tarde', 'Noite', 'Plantão 12h', 'Plantão 24h')),
    id_residente INT NOT NULL REFERENCES RESIDENTE(id_pessoa) ON DELETE CASCADE,
    id_preceptor INT NOT NULL REFERENCES PRECEPTOR(id_pessoa) ON DELETE CASCADE,
    CONSTRAINT uq_escala_residente UNIQUE (id_unidade, dia_semana, turno, id_residente) -- Garante que um residente não tenha escalas duplicadas no mesmo horário/unidade
);