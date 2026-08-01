package ufpb.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import ufpb.hospital.model.Escala;
import ufpb.hospital.model.Preceptor;
import ufpb.hospital.model.Residente;
import ufpb.hospital.model.UnidadeSaude;
import ufpb.hospital.repository.EscalaRepository;

@Service
public class EscalaService {

    private final EscalaRepository escalaRepository;
    private final EntityManager entityManager;

    public EscalaService(EscalaRepository escalaRepository, EntityManager entityManager) {
        this.escalaRepository = escalaRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Escala alocarResidenteEmEscala(Integer idResidente, Integer idPreceptor, Integer idUnidade, String diaSemana, String turno) {
        Escala escala = new Escala();
        
        // Obtém as referências das entidades mapeadas via JPA
        escala.setResidente(entityManager.getReference(Residente.class, idResidente));
        escala.setPreceptor(entityManager.getReference(Preceptor.class, idPreceptor));
        escala.setUnidadeSaude(entityManager.getReference(UnidadeSaude.class, idUnidade));
        escala.setDiaSemana(diaSemana);
        escala.setTurno(turno);

        // O Hibernate faz o INSERT e ativa a trigger trg_check_sobreposicao_escala no Postgres
        return escalaRepository.save(escala);
    }

    // Método para invocar a Stored Procedure sp_reajustar_escala
    @Transactional
    public void reajustarEscala(Integer idResidente, String diaOrigem, String turnoOrigem, String diaDestino, String turnoDestino) {
        String sql = "CALL public.sp_reajustar_escala(:p_id_residente, :p_dia_antigo, :p_turno_antigo, :p_dia_novo, :p_turno_novo)";
        
        entityManager.createNativeQuery(sql)
                .setParameter("p_id_residente", idResidente)
                .setParameter("p_dia_antigo", diaOrigem)
                .setParameter("p_turno_antigo", turnoOrigem)
                .setParameter("p_dia_novo", diaDestino)
                .setParameter("p_turno_novo", turnoDestino)
                .executeUpdate();
    }

    @Transactional
    public void simularConcorrenciaAlocacao(Integer idResidente, Integer idPreceptor, Integer idUnidade, String dia, String turno) {
        // Valida se já existe para evitar inconsistência via aplicação antes de persistir
        boolean existe = escalaRepository.existsConflitoResidente(idResidente, dia, turno);
        if (existe) {
            throw new IllegalStateException("Conflito detectado: Residente já escalado no mesmo dia e turno!");
        }
        alocarResidenteEmEscala(idResidente, idPreceptor, idUnidade, dia, turno);
    }
}