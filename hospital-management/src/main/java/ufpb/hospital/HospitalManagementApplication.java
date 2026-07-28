package ufpb.hospital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ufpb.hospital.service.AtendimentoService;

@SpringBootApplication
public class HospitalManagementApplication implements CommandLineRunner {

    private final AtendimentoService atendimentoService;

    public HospitalManagementApplication(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- [TESTE STORED PROCEDURE] Registrar Atendimento Completo ---");

        try {
            // JSON simulando a lista de procedimentos realizados
            String jsonProcedimentos = "[{\"id_procedimento\": 1, \"tempo_real\": 25}, {\"id_procedimento\": 2, \"tempo_real\": 40}]";

            // IDs existentes no seu banco
            atendimentoService.registrarAtendimentoCompleto(
                    1,                  // idPaciente
                    6,                  // idResidente
                    11,                 // idPreceptor
                    1,                  // idUnidade
                    65,                 // duracaoMinutos
                    jsonProcedimentos   // <-- Usando a variável aqui
            );

            System.out.println("✅ Atendimento e procedimentos registrados com sucesso via Stored Procedure!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao registrar atendimento: " + e.getMessage());
        }

        System.out.println("----------------------------------------------------------------\n");
    }
}