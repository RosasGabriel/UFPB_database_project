package ufpb.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufpb.hospital.model.Escala;

public interface EscalaRepository extends JpaRepository<Escala, Integer> {

    // Busca padronizada com Integer
    List<Escala> findByResidenteIdPessoa(Integer idResidente);

    @Query("SELECT COUNT(e) > 0 FROM Escala e " +
           "WHERE e.residente.idPessoa = :idResidente " +
           "AND e.diaSemana = :diaSemana " +
           "AND e.turno = :turno")
    boolean existsConflitoResidente(@Param("idResidente") Integer idResidente, 
                                    @Param("diaSemana") String diaSemana, 
                                    @Param("turno") String turno);
}