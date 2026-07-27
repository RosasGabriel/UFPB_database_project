package ufpb.hospital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ufpb.hospital.repository.ResidenteRepository;
import ufpb.hospital.service.EscalaService;

@SpringBootApplication
public class HospitalManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner testarAplicacao(
            ResidenteRepository residenteRepository,
            EscalaService escalaService) {
        return args -> {
            System.out.println("\n--------------------------------------------------");
            System.out.println("=== TESTE DE INTEGRAÇÃO SPRING + POSTGRESQL ===");
            
            // 1. Consulta JPA Básica
            long total = residenteRepository.count();
            System.out.println("Residentes cadastrados no banco: " + total);
            
            residenteRepository.findAll().forEach(r -> 
			System.out.println("-> Residente ID " + r.getIdPessoa() + ": " + r.getNome() + " | Torce Flamengo? " + r.getIsFlamengo())
		);

            System.out.println("--------------------------------------------------\n");
        };
    }
}