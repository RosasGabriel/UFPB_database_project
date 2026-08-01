package ufpb.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AtendimentoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void registrarAtendimentoCompleto(
            Integer idPaciente, 
            Integer idResidente, 
            Integer idPreceptor, 
            Integer idUnidade, 
            Integer duracaoMinutos, 
            String procedimentosJson) {

        String sql = "CALL public.sp_registrar_atendimento_completo(" +
                     ":p_id_paciente, " +
                     ":p_id_residente, " +
                     ":p_id_preceptor, " +
                     ":p_id_unidade, " +
                     ":p_duracao, " +
                     "CAST(:p_procedimentos AS jsonb))";

        entityManager.createNativeQuery(sql)
                .setParameter("p_id_paciente", idPaciente)
                .setParameter("p_id_residente", idResidente)
                .setParameter("p_id_preceptor", idPreceptor)
                .setParameter("p_id_unidade", idUnidade)
                .setParameter("p_duracao", duracaoMinutos)
                .setParameter("p_procedimentos", procedimentosJson)
                .executeUpdate();
    }

    @Transactional
    public void calcularTempoMedioEspera() {
        String sql = "CALL public.sp_calcular_tempo_medio_espera()";
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}