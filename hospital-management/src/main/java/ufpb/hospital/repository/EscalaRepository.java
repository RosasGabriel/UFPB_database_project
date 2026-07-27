package ufpb.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import ufpb.hospital.model.Escala;

public interface EscalaRepository extends JpaRepository<Escala, Long> {

    // Chama diretamente a Stored Procedure criada no PostgreSQL
    @Procedure(procedureName = "sp_alocar_residente")
    void alocarResidente(
        @Param("p_id_residente") Long idResidente,
        @Param("p_id_preceptor") Long idPreceptor,
        @Param("p_id_unidade") Long idUnidade,
        @Param("p_dia_semana") String diaSemana,
        @Param("p_turno") String turno
    );

    // Consulta JPA personalizada para buscar escalas de um residente
    List<Escala> findByResidenteIdPessoa(Long idResidente);
}