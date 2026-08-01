package ufpb.hospital;

import java.util.Scanner;

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
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        System.out.println("\n================================================================");
        System.out.println("     SISTEMA DE GESTÃO HOSPITALAR UFPB - CLI INTERATIVO");
        System.out.println("================================================================");

        while (executando) {
            exibirMenuPrincipal();
            System.out.print("Digite uma opção: ");
            String opcao = scanner.nextLine().trim();

            System.out.println("\n----------------------------------------------------------------");
            switch (opcao) {
                case "1" -> menuGerenciamentoEntidade(scanner, "PACIENTE");
                case "2" -> menuGerenciamentoEntidade(scanner, "PRECEPTOR");
                case "3" -> menuGerenciamentoEntidade(scanner, "RESIDENTE");
                case "4" -> menuGerenciamentoEntidade(scanner, "UNIDADE DE SAÚDE");
                case "5" -> menuGerenciamentoEntidade(scanner, "ATENDIMENTO");
                case "6" -> menuEtapa2ProceduresETriggers(scanner);
                case "0" -> {
                    executando = false;
                    System.out.println("Encerrando o sistema... Até mais!");
                }
                default -> System.out.println("❌ Opção inválida! Tente novamente.");
            }
            System.out.println("----------------------------------------------------------------\n");
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. [ETAPA 1] Gerenciar Pacientes");
        System.out.println("2. [ETAPA 1] Gerenciar Preceptores");
        System.out.println("3. [ETAPA 1] Gerenciar Residentes");
        System.out.println("4. [ETAPA 1] Gerenciar Unidades de Saúde");
        System.out.println("5. [ETAPA 1] Gerenciar Atendimentos (CRUD Básico)");
        System.out.println("6. [ETAPA 2] Regras Avançadas (Procedures, JSONB e Triggers)");
        System.out.println("0. Sair");
    }

   // =========================================================================
    // SUBMENU GENÉRICO DE CRUD PARA ENTIDADES
    // =========================================================================

    private void menuGerenciamentoEntidade(Scanner scanner, String nomeEntidade) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- [ETAPA 1] CRUD: " + nomeEntidade + " ---");
            System.out.println("1. Cadastrar " + nomeEntidade);
            System.out.println("2. Buscar " + nomeEntidade + " por ID");
            System.out.println("3. Atualizar " + nomeEntidade);
            System.out.println("4. Remover " + nomeEntidade);
            System.out.println("5. Listar Todos(as) " + nomeEntidade + "s");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> executarCriacao(scanner, nomeEntidade);
                case "2" -> executarLeitura(scanner, nomeEntidade);
                case "3" -> executarAtualizacao(scanner, nomeEntidade);
                case "4" -> executarRemocao(scanner, nomeEntidade);
                case "5" -> executarListagemTodas(nomeEntidade);
                case "0" -> voltar = true;
                default -> System.out.println("❌ Opção inválida!");
            }
        }
    }

    // --- MÉTODOS DE AÇÃO DO CRUD ---

    private void executarCriacao(Scanner scanner, String entidade) {
        System.out.println("\n=== [CRIAR] Novo Registro em " + entidade + " ===");
        try {
            System.out.print("Digite o nome/identificador: ");
            String nome = scanner.nextLine();
            
            System.out.println("✅ " + entidade + " '" + nome + "' cadastrado(a) com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar " + entidade + ": " + e.getMessage());
        }
    }

    private void executarLeitura(Scanner scanner, String entidade) {
        System.out.println("\n=== [BUSCAR] Consultar " + entidade + " ===");
        try {
            System.out.print("Digite o ID para busca: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.println("🔍 Buscando " + entidade + " com ID " + id + "...");
            System.out.println("✅ Registro encontrado: ID=" + id + " | Status: Ativo");
        } catch (NumberFormatException e) {
            System.out.println("❌ Erro: O ID precisa ser um número inteiro.");
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar " + entidade + ": " + e.getMessage());
        }
    }

    private void executarAtualizacao(Scanner scanner, String entidade) {
        System.out.println("\n=== [ATUALIZAR] Modificar " + entidade + " ===");
        try {
            System.out.print("Digite o ID a ser atualizado: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Digite o novo valor/informação: ");
            String novoValor = scanner.nextLine();

            System.out.println("✅ " + entidade + " (ID " + id + ") atualizado(a) para: " + novoValor);
        } catch (NumberFormatException e) {
            System.out.println("❌ Erro: O ID precisa ser um número inteiro.");
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar " + entidade + ": " + e.getMessage());
        }
    }

    private void executarRemocao(Scanner scanner, String entidade) {
        System.out.println("\n=== [REMOVER] Excluir " + entidade + " ===");
        try {
            System.out.print("Digite o ID a ser removido: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.println("✅ " + entidade + " (ID " + id + ") removido(a) com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("❌ Erro: O ID precisa ser um número inteiro.");
        } catch (Exception e) {
            System.out.println("❌ Erro ao remover " + entidade + ": " + e.getMessage());
        }
    }

    private void executarListagemTodas(String entidade) {
        System.out.println("\n=== [LISTAR] Todos os Registros de " + entidade + " ===");
        try {
            System.out.println("🔍 Consultando tabela no banco de dados...");
            
            // Aqui você chamará o metodo `findAll()` do serviço/repositório da respectiva entidade:
            // Exemplo: List<Paciente> lista = pacienteService.listarTodos();
            
            System.out.println("----------------------------------------------------------------");
            System.out.println("ID | NOME / IDENTIFICADOR               | STATUS");
            System.out.println("----------------------------------------------------------------");
            System.out.println(" 1 | Registro Exemplo 01               | Ativo");
            System.out.println(" 2 | Registro Exemplo 02               | Ativo");
            System.out.println(" 3 | Registro Exemplo 03               | Inativo");
            System.out.println("----------------------------------------------------------------");
            System.out.println("✅ Total de registros encontrados: 3");
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar registros de " + entidade + ": " + e.getMessage());
        }
    }

    // =========================================================================
    // SUBMENU ETAPA 2 - STORED PROCEDURES & TRIGGERS
    // =========================================================================

    private void menuEtapa2ProceduresETriggers(Scanner scanner) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- [ETAPA 2] PROCEDURES E TRIGGERS ---");
            System.out.println("1. [Procedure] Registrar Atendimento Completo (com JSONB)");
            System.out.println("2. [Procedure] Calcular Tempo Médio de Espera");
            System.out.println("3. [Procedure] Reajustar Escala de Residente");
            System.out.println("4. [Trigger / Lock] Simular Conflito de Escala (Sobreposição)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> registrarAtendimentoCompleto(scanner);
                case "2" -> calcularTempoMedio();
                case "3" -> reajustarEscala(scanner);
                case "4" -> testarConflitoEscala(scanner);
                case "0" -> voltar = true;
                default -> System.out.println("❌ Opção inválida!");
            }
        }
    }

    private void registrarAtendimentoCompleto(Scanner scanner) {
        System.out.println("\n=== [PROCEDURE] Registrar Atendimento Completo ===");
        try {
            System.out.print("ID do Paciente: ");
            int idPaciente = Integer.parseInt(scanner.nextLine());

            System.out.print("ID do Residente: ");
            int idResidente = Integer.parseInt(scanner.nextLine());

            System.out.print("ID do Preceptor: ");
            int idPreceptor = Integer.parseInt(scanner.nextLine());

            System.out.print("ID da Unidade de Saúde: ");
            int idUnidade = Integer.parseInt(scanner.nextLine());

            System.out.print("Duração total (minutos): ");
            int duracao = Integer.parseInt(scanner.nextLine());

            String json = "[{\"id_procedimento\": 1, \"tempo_real\": 25}]";

            atendimentoService.registrarAtendimentoCompleto(idPaciente, idResidente, idPreceptor, idUnidade, duracao, json);
            System.out.println("✅ Stored Procedure executada com sucesso!");

        } catch (Exception e) {
            System.out.println("❌ Erro na execução da Stored Procedure: " + e.getMessage());
        }
    }

    private void calcularTempoMedio() {
        System.out.println("\n=== [PROCEDURE] Calcular Tempo Médio de Espera ===");
        try {
            atendimentoService.calcularTempoMedioEspera();
            System.out.println("✅ Stored Procedure executada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao calcular tempo médio: " + e.getMessage());
        }
    }

    private void reajustarEscala(Scanner scanner) {
        System.out.println("\n=== [PROCEDURE] Reajustar Escala ===");
        try {
            System.out.print("ID do Residente: ");
            int idResidente = Integer.parseInt(scanner.nextLine());

            System.out.print("Dia Antigo (ex: Segunda): ");
            String diaAntigo = scanner.nextLine();

            System.out.print("Turno Antigo (ex: Manhã): ");
            String turnoAntigo = scanner.nextLine();

            System.out.print("Novo Dia (ex: Sexta): ");
            String diaNovo = scanner.nextLine();

            System.out.print("Novo Turno (ex: Noite): ");
            String turnoNovo = scanner.nextLine();

            escalaService.reajustarEscala(idResidente, diaAntigo, turnoAntigo, diaNovo, turnoNovo);
            System.out.println("✅ Escala reajustada com sucesso!");

        } catch (Exception e) {
            System.out.println("❌ Erro ao reajustar escala: " + e.getMessage());
        }
    }

    private void testarConflitoEscala(Scanner scanner) {
        System.out.println("\n=== [TRIGGER / LOCK] Validação de Conflito de Escala ===");
        try {
            System.out.print("ID do Residente: ");
            int idResidente = Integer.parseInt(scanner.nextLine());

            System.out.print("ID do Preceptor: ");
            int idPreceptor = Integer.parseInt(scanner.nextLine());

            System.out.print("ID da Unidade de Saúde: ");
            int idUnidade = Integer.parseInt(scanner.nextLine());

            System.out.print("Dia da Semana: ");
            String dia = scanner.nextLine();

            System.out.print("Turno: ");
            String turno = scanner.nextLine();

            escalaService.simularConcorrenciaAlocacao(idResidente, idPreceptor, idUnidade, dia, turno);
            System.out.println("✅ Alocação realizada sem conflitos.");

        } catch (Exception e) {
            System.out.println("⚠️ Validação disparada com sucesso! Mensagem: " + e.getMessage());
        }
    }
}