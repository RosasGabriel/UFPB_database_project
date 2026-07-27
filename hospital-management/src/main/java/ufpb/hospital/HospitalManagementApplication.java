package ufpb.hospital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ufpb.hospital.service.EscalaService;

@SpringBootApplication
public class HospitalManagementApplication implements CommandLineRunner {

    private final EscalaService escalaService;

    public HospitalManagementApplication(EscalaService escalaService) {
        this.escalaService = escalaService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- [TESTE ORM + TRIGGER] Alocação de Escala ---");

        try {
            // Supondo IDs válidos no seu banco: Residente 6, Preceptor 1, Unidade 1
            // Usando um dia/turno específico para o teste (ex: "Sábado", "Noite")
            System.out.println("1. Tentando alocar residente em escala via JPA...");
            var escalaCriada = escalaService.alocarResidenteEmEscala(6, 1, 1, "Sábado", "Noite");
            System.out.println("✅ Sucesso! Escala criada com ID: " + escalaCriada.getIdEscala());

            // 2. Teste do Trigger: tentar escalar o MESMO residente no MESMO dia/turno em OUTRA unidade (ex: Unidade 2)
            System.out.println("\n2. Tentando conflito de escala (mesmo dia/turno, unidade diferente)...");
            escalaService.alocarResidenteEmEscala(6, 1, 2, "Sábado", "Noite");
            System.out.println("❌ ERRO: A trigger deveria ter impedido!");

        } catch (Exception e) {
            System.out.println("✅ Trigger funcionou perfeitamente!");
            System.out.println("Mensagem capturada: " + e.getMessage());
        }

        System.out.println("------------------------------------------------\n");
    }
}