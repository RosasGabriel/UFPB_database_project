package ufpb.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import ufpb.hospital.model.Atendimento;
import ufpb.hospital.model.Preceptor;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {
    
    @Procedure(procedureName = "sp_calcular_tempo_medio_espera")
    Double calcularTempoMedioEspera();
    
    // 1. Listar todos os preceptores que supervisionaram residentes que atenderam pacientes flamenguistas (is_flamengo = TRUE)
    @Query("SELECT DISTINCT p FROM Preceptor p " +
           "JOIN Atendimento a ON a.preceptor = p " +
           "JOIN Paciente pac ON a.paciente = pac " +
           "WHERE pac.isFlamengo = TRUE")
    List<Preceptor> findPreceptoresDePacientesFlamenguistas();

    // 2. Para cada paciente, exibir seu último atendimento (data_hora, residente, preceptor, lista de procedimentos)[cite: 18]
    @Query("SELECT a FROM Atendimento a " +
           "WHERE a.dataHora = (SELECT MAX(a2.dataHora) FROM Atendimento a2 WHERE a2.paciente = a.paciente)")
    List<Atendimento> findUltimoAtendimentoPorPaciente();

    // 3. Calcular o percentual de procedimentos de alto risco realizados por cada residente[cite: 18]
    @Query("SELECT r.nome, " +
           "(SUM(CASE WHEN pr.procedimento.tempoMedioExecucao > 60 THEN 1 ELSE 0 END) * 100.0 / COUNT(pr)) " +
           "FROM ProcedimentoRealizado pr " +
           "JOIN pr.atendimento a " +
           "JOIN a.residente r " +
           "GROUP BY r.idPessoa, r.nome")
    List<Object[]> calcularPercentualProcedimentosAltoRiscoPorResidente();

    // =========================================================================
    // CONSULTAS ÀS VIEWS DO POSTGRESQL (NATIVE QUERY)
    // =========================================================================

    @Query(value = "SELECT * FROM vw_pacientes_internados", nativeQuery = true)
    List<Object[]> consultarViewPacientesInternados();

    @Query(value = "SELECT * FROM vw_residentes_sem_supervisor", nativeQuery = true)
    List<Object[]> consultarViewResidentesSemSupervisor();

    @Query(value = "SELECT * FROM vw_estatisticas_atendimentos_mensal", nativeQuery = true)
    List<Object[]> consultarViewEstatisticasMensais();
    
}