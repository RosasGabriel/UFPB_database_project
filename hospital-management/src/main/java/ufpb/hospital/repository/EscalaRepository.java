package ufpb.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.Escala;

public interface EscalaRepository extends JpaRepository<Escala, Integer> {

    // Busca via DSL do Spring Data JPA (sem SQL cru)
    List<Escala> findByResidenteIdPessoa(Integer idResidente);
}