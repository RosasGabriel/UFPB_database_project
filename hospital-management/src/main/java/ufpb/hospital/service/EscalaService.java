package ufpb.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufpb.hospital.repository.EscalaRepository;

@Service
public class EscalaService {

    private final EscalaRepository escalaRepository;

    public EscalaService(EscalaRepository escalaRepository) {
        this.escalaRepository = escalaRepository;
    }

    @Transactional
    public void alocarResidenteEmEscala(Long idResidente, Long idPreceptor, Long idUnidade, String diaSemana, String turno) {
        // Invoca a Stored Procedure do PostgreSQL através do Repository
        escalaRepository.alocarResidente(idResidente, idPreceptor, idUnidade, diaSemana, turno);
    }
}