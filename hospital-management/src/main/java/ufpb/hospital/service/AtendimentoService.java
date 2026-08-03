package ufpb.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ufpb.hospital.repository.AtendimentoRepository;

@Service
public class AtendimentoService {

    @PersistenceContext
    private EntityManager entityManager;

    // 1. Declarando o repositório
    private final AtendimentoRepository atendimentoRepository;

    // 2. Injetando o repositório via construtor
    public AtendimentoService(AtendimentoRepository atendimentoRepository) {
        this.atendimentoRepository = atendimentoRepository;
    }

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
    public Double calcularTempoMedioEspera() {
        return atendimentoRepository.calcularTempoMedioEspera();
    }
}