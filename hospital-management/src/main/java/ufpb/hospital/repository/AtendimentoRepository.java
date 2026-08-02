package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import ufpb.hospital.model.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {
    
    @Procedure(procedureName = "sp_calcular_tempo_medio_espera")
    Double calcularTempoMedioEspera();
    
}