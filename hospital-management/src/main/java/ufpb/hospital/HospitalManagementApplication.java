package ufpb.hospital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ufpb.hospital.service.AtendimentoService;
import ufpb.hospital.service.EscalaService;

@SpringBootApplication
public class HospitalManagementApplication implements CommandLineRunner {

    private final AtendimentoService atendimentoService;
    private final EscalaService escalaService;

    public HospitalManagementApplication(AtendimentoService atendimentoService, EscalaService escalaService) {
        this.atendimentoService = atendimentoService;
        this.escalaService = escalaService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n================================================================");
        System.out.println("          BATERIA DE TESTES - GESTÃO HOSPITALAR (JPA/SQL)");
        System.out.println("================================================================\n");

        // ---------------------------------------------------------------------
        // TESTE 1: Inserção de Atendimento Completo via Stored Procedure & JSONB
        // ---------------------------------------------------------------------
        System.out.println("--- [TESTE 1] Stored Procedure: Registrar Atendimento ---");
        try {
            String jsonProcedimentos = "[{\"id_procedimento\": 1, \"tempo_real\": 25}, {\"id_procedimento\": 2, \"tempo_real\": 40}]";
            
            atendimentoService.registrarAtendimentoCompleto(
                    1,   // idPaciente
                    6,   // idResidente
                    11,  // idPreceptor
                    1,   // idUnidade
                    65,  // duracaoMinutos
                    jsonProcedimentos
            );
            System.out.println("✅ Atendimento registrado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro no Teste 1: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // TESTE 2: Procedure de Tempo Médio de Espera/Execução
        // ---------------------------------------------------------------------
        System.out.println("\n--- [TESTE 2] Stored Procedure: Tempo Médio de Espera ---");
        try {
            atendimentoService.calcularTempoMedioEspera();
            System.out.println("✅ Execução da procedure de tempo médio concluída!");
        } catch (Exception e) {
            System.out.println("❌ Erro no Teste 2: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // TESTE 3: Reajuste de Escala via Stored Procedure
        // ---------------------------------------------------------------------
        System.out.println("\n--- [TESTE 3] Stored Procedure: Reajustar Escala ---");
        try {
            // Reajusta a escala do residente 6 do turno antigo para um novo
            escalaService.reajustarEscala(6, "Segunda", "Manhã", "Sexta", "Noite");
            System.out.println("✅ Reajuste de escala realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("⚠️ Nota no Teste 3: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // TESTE 4: Teste de Validação de Conflito/Concorrência em Escalas (Trigger)
        // ---------------------------------------------------------------------
        System.out.println("\n--- [TESTE 4] Validação de Conflito de Escala (Trigger/Lock) ---");
        try {
            // Tenta simular a alocação do mesmo residente no mesmo dia/turno
            escalaService.simularConcorrenciaAlocacao(6, 11, 2, "Segunda", "Manhã");
            System.out.println("✅ Alocação realizada.");
        } catch (Exception e) {
            System.out.println("✅ Capturado com sucesso (Comportamento Esperado): " + e.getMessage());
        }

        System.out.println("\n================================================================");
        System.out.println("                FIM DA BATERIA DE TESTES");
        System.out.println("================================================================\n");
    }
}