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
}