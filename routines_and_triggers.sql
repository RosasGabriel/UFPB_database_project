-- =============================================================================
-- 1. VIEWS (Visualizações para relatórios)
-- =============================================================================

-- 1.1 View: Pacientes atualmente internados
CREATE OR REPLACE VIEW vw_pacientes_internados AS
SELECT 
    p.id_pessoa,
    p.nome AS nome_paciente,
    pac.num_convenio,
    a.id_atendimento,
    a.data_hora AS data_internacao,
    u.nome AS unidade
FROM PACIENTE pac
JOIN PESSOA p ON pac.id_pessoa = p.id_pessoa
JOIN ATENDIMENTO a ON pac.id_pessoa = a.id_paciente
JOIN UNIDADE u ON a.id_unidade = u.id_unidade
WHERE u.tipo = 'Internação'; -- Filtro para unidades de internação/enfermaria

-- 1.2 View: Residentes escalados cujo preceptor não tem titulação de Doutor
CREATE OR REPLACE VIEW vw_residentes_sem_supervisor AS
SELECT DISTINCT
    r_p.nome AS nome_residente,
    e.dia_semana,
    e.turno,
    u.nome AS unidade,
    p_p.nome AS nome_preceptor,
    prec.titulacao
FROM ESCALA e
JOIN RESIDENTE r ON e.id_residente = r.id_pessoa
JOIN PESSOA r_p ON r.id_pessoa = r_p.id_pessoa
JOIN PRECEPTOR prec ON e.id_preceptor = prec.id_pessoa
JOIN PESSOA p_p ON prec.id_pessoa = p_p.id_pessoa
JOIN UNIDADE u ON e.id_unidade = u.id_unidade
WHERE UPPER(prec.titulacao) NOT LIKE '%DOUTOR%';

-- 1.3 View: Agregação estatística de atendimentos por mês e unidade
CREATE OR REPLACE VIEW vw_estatisticas_atendimentos_mensal AS
SELECT 
    EXTRACT(YEAR FROM a.data_hora) AS ano,
    EXTRACT(MONTH FROM a.data_hora) AS mes,
    u.nome AS unidade,
    COUNT(a.id_atendimento) AS total_atendimentos,
    ROUND(AVG(a.duracao_minutos), 2) AS media_duracao_minutos
FROM ATENDIMENTO a
JOIN UNIDADE u ON a.id_unidade = u.id_unidade
GROUP BY EXTRACT(YEAR FROM a.data_hora), EXTRACT(MONTH FROM a.data_hora), u.nome;


-- =============================================================================
-- 2. TRIGGERS (Gatilhos de Integridade e Auditoria)
-- =============================================================================

-- 2.1 Trigger: Impede que o mesmo residente seja escalado no mesmo dia/turno em unidades diferentes
CREATE OR REPLACE FUNCTION fn_check_sobreposicao_escala()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM ESCALA 
        WHERE id_residente = NEW.id_residente 
          AND dia_semana = NEW.dia_semana 
          AND turno = NEW.turno 
          AND id_unidade <> NEW.id_unidade
          AND (TG_OP = 'INSERT' OR id_escala <> NEW.id_escala)
    ) THEN
        RAISE EXCEPTION 'Erro de escala: O residente já possui plantão escalado no mesmo dia (%) e turno (%) em outra unidade.', NEW.dia_semana, NEW.turno;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_sobreposicao_escala ON ESCALA;
CREATE TRIGGER trg_check_sobreposicao_escala
BEFORE INSERT OR UPDATE ON ESCALA
FOR EACH ROW EXECUTE FUNCTION fn_check_sobreposicao_escala();


-- 2.2 Trigger: Auditoria de alterações na tabela ATENDIMENTO
-- Tabela de suporte para auditoria
CREATE TABLE IF NOT EXISTS AUDITORIA_ATENDIMENTO (
    id_auditoria SERIAL PRIMARY KEY,
    id_atendimento INT NOT NULL,
    operacao VARCHAR(10) NOT NULL,
    usuario VARCHAR(50) NOT NULL DEFAULT CURRENT_USER,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dados_antigos JSONB,
    dados_novos JSONB
);

CREATE OR REPLACE FUNCTION fn_audita_atendimento()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO AUDITORIA_ATENDIMENTO(id_atendimento, operacao, dados_antigos)
        VALUES (OLD.id_atendimento, 'DELETE', to_jsonb(OLD));
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO AUDITORIA_ATENDIMENTO(id_atendimento, operacao, dados_antigos, dados_novos)
        VALUES (NEW.id_atendimento, 'UPDATE', to_jsonb(OLD), to_jsonb(NEW));
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO AUDITORIA_ATENDIMENTO(id_atendimento, operacao, dados_novos)
        VALUES (NEW.id_atendimento, 'INSERT', to_jsonb(NEW));
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_audita_atendimento ON ATENDIMENTO;
CREATE TRIGGER trg_audita_atendimento
AFTER INSERT OR UPDATE OR DELETE ON ATENDIMENTO
FOR EACH ROW EXECUTE FUNCTION fn_audita_atendimento();


-- 2.3 Trigger: Atualização da média de tempo do procedimento
-- Adicionando a coluna calculada na tabela PROCEDIMENTO
ALTER TABLE PROCEDIMENTO ADD COLUMN IF NOT EXISTS media_tempo_procedimento NUMERIC(10,2) DEFAULT 0;

CREATE OR REPLACE FUNCTION fn_atualiza_media_procedimentos()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE PROCEDIMENTO
    SET media_tempo_procedimento = (
        SELECT ROUND(AVG(tempo_real_minutos), 2)
        FROM PROCEDIMENTO_REALIZADO
        WHERE id_procedimento = NEW.id_procedimento
    )
    WHERE id_procedimento = NEW.id_procedimento;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_atualiza_media_procedimentos ON PROCEDIMENTO_REALIZADO;
CREATE TRIGGER trg_atualiza_media_procedimentos
AFTER INSERT OR UPDATE ON PROCEDIMENTO_REALIZADO
FOR EACH ROW EXECUTE FUNCTION fn_atualiza_media_procedimentos();


-- =============================================================================
-- 3. STORED PROCEDURES (Regras Transacionais de Negócio)
-- =============================================================================

-- 3.1 Procedure: Reajuste seguro de escalas
CREATE OR REPLACE PROCEDURE sp_reajustar_escala(
    p_id_residente INT,
    p_dia_antigo VARCHAR,
    p_turno_antigo VARCHAR,
    p_dia_novo VARCHAR,
    p_turno_novo VARCHAR
)
LANGUAGE plpgsql AS $$
BEGIN
    -- A validação de conflitos será tratada automaticamente pela Trigger trg_check_sobreposicao_escala
    UPDATE ESCALA
    SET dia_semana = p_dia_novo,
        turno = p_turno_novo
    WHERE id_residente = p_id_residente
      AND dia_semana = p_dia_antigo
      AND turno = p_turno_antigo;

    IF NOT FOUND THEN
        RAISE NOTICE 'Nenhuma escala foi encontrada para o residente % no dia % e turno %.', p_id_residente, p_dia_antigo, p_turno_antigo;
    END IF;
END;
$$;

-- 3.2 Procedure: Registro completo de atendimento com transação e JSON
CREATE OR REPLACE PROCEDURE sp_registrar_atendimento_completo(
    p_id_paciente INT,
    p_id_residente INT,
    p_id_preceptor INT,
    p_id_unidade INT,
    p_duracao_minutos INT,
    p_procedimentos JSONB -- Exemplo: '[{"id_procedimento": 1, "tempo_real": 30}, {"id_procedimento": 2, "tempo_real": 45}]'
)
LANGUAGE plpgsql AS $$
DECLARE
    v_id_atendimento INT;
    v_proc JSONB;
BEGIN
    -- 1. Insere o atendimento principal
    INSERT INTO ATENDIMENTO (id_paciente, id_residente, id_preceptor, id_unidade, duracao_minutos, data_hora)
    VALUES (p_id_paciente, p_id_residente, p_id_preceptor, p_id_unidade, p_duracao_minutos, CURRENT_TIMESTAMP)
    RETURNING id_atendimento INTO v_id_atendimento;

    -- 2. Itera sobre a lista de procedimentos em JSON e insere em PROCEDIMENTO_REALIZADO
    IF p_procedimentos IS NOT NULL AND jsonb_array_length(p_procedimentos) > 0 THEN
        FOR v_proc IN SELECT * FROM jsonb_array_elements(p_procedimentos)
        LOOP
            INSERT INTO PROCEDIMENTO_REALIZADO (id_atendimento, id_procedimento, tempo_real_minutos)
            VALUES (
                v_id_atendimento, 
                (v_proc->>'id_procedimento')::INT, 
                (v_proc->>'tempo_real')::INT
            );
        END LOOP;
    END IF;

    -- Qualquer erro durante a iteração faz o PL/pgSQL reverter (rollback) toda a bloco transacional
END;
$$;