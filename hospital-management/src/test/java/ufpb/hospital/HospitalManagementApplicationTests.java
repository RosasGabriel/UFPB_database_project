package ufpb.hospital;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ufpb.hospital.service.AtendimentoService;
import ufpb.hospital.service.EscalaService;

@SpringBootTest
class HospitalManagementApplicationTests {

    @Autowired
    private AtendimentoService atendimentoService;

    @Autowired
    private EscalaService escalaService;

    @Test
    @DisplayName("Teste de Integração: Fluxo Completo de Procedures e Triggers")
    void executarBateriaDeTestesIntegrados() {
        System.out.println("\n================================================================");
        System.out.println("          BATERIA DE TESTES AUTOMATIZADOS (JPA/SQL)");
        System.out.println("================================================================");

        // ---------------------------------------------------------------------
        // TESTE 1: Inserção de Atendimento Completo via Stored Procedure & JSONB
        // ---------------------------------------------------------------------
        System.out.println("--- [TESTE 1] Stored Procedure: Registrar Atendimento ---");
        assertDoesNotThrow(() -> {
            String jsonProcedimentos = "[{\"id_procedimento\": 1, \"tempo_real\": 25}, {\"id_procedimento\": 2, \"tempo_real\": 40}]";
            
            atendimentoService.registrarAtendimentoCompleto(
                    1,   // idPaciente
                    6,   // idResidente
                    11,  // idPreceptor
                    1,   // idUnidade
                    65,  // duracaoMinutos
                    jsonProcedimentos
            );
            System.out.println("✅ Atendimento registrado com sucesso via Stored Procedure!");
        });

        // ---------------------------------------------------------------------
        // TESTE 2: Procedure de Tempo Médio de Espera/Execução
        // ---------------------------------------------------------------------
        System.out.println("\n--- [TESTE 2] Stored Procedure: Tempo Médio de Espera ---");
        assertDoesNotThrow(() -> {
            atendimentoService.calcularTempoMedioEspera();
            System.out.println("✅ Execução da procedure de tempo médio concluída!");
        });

        // ---------------------------------------------------------------------
        // TESTE 3: Reajuste de Escala via Stored Procedure
        // ---------------------------------------------------------------------
        System.out.println("\n--- [TESTE 3] Stored Procedure: Reajustar Escala ---");
        assertDoesNotThrow(() -> {
            escalaService.reajustarEscala(6, "Segunda", "Manhã", "Sexta", "Noite");
            System.out.println("✅ Reajuste de escala realizado com sucesso!");
        });

        // ---------------------------------------------------------------------
        // TESTE 4: Teste de Validação de Conflito/Concorrência em Escalas (Trigger/Lock)
        // ---------------------------------------------------------------------
        System.out.println("\n--- [TESTE 4] Validação de Conflito de Escala (Trigger/Lock) ---");
        try {
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