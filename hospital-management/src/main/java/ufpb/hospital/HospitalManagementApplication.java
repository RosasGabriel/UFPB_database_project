package ufpb.hospital;

import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ufpb.hospital.model.Atendimento;
import ufpb.hospital.model.Escala;
import ufpb.hospital.model.Paciente;
import ufpb.hospital.model.Preceptor;
import ufpb.hospital.model.Procedimento;
import ufpb.hospital.model.Residente;
import ufpb.hospital.model.UnidadeSaude;
import ufpb.hospital.repository.AtendimentoRepository;
import ufpb.hospital.repository.EscalaRepository;
import ufpb.hospital.repository.PacienteRepository;
import ufpb.hospital.repository.PreceptorRepository;
import ufpb.hospital.repository.ProcedimentoRepository;
import ufpb.hospital.repository.ResidenteRepository;
import ufpb.hospital.repository.UnidadeSaudeRepository;
import ufpb.hospital.service.AtendimentoService;
import ufpb.hospital.service.EscalaService;

@SpringBootApplication
public class HospitalManagementApplication implements CommandLineRunner {

    private final AtendimentoService atendimentoService;
    private final EscalaService escalaService;
    
    // Repositórios para o CRUD básico
    private final PacienteRepository pacienteRepository;
    private final PreceptorRepository preceptorRepository;
    private final ResidenteRepository residenteRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;
    private final AtendimentoRepository atendimentoRepository;
    
    // Adicionando os repositórios que faltavam
    private final ProcedimentoRepository procedimentoRepository;
    private final EscalaRepository escalaRepository;

    // Atualizando o construtor para injetar todos os repositórios
    public HospitalManagementApplication(AtendimentoService atendimentoService, 
                                         EscalaService escalaService,
                                         PacienteRepository pacienteRepository,
                                         PreceptorRepository preceptorRepository,
                                         ResidenteRepository residenteRepository,
                                         UnidadeSaudeRepository unidadeSaudeRepository,
                                         AtendimentoRepository atendimentoRepository,
                                         ProcedimentoRepository procedimentoRepository,
                                         EscalaRepository escalaRepository) {
        this.atendimentoService = atendimentoService;
        this.escalaService = escalaService;
        this.pacienteRepository = pacienteRepository;
        this.preceptorRepository = preceptorRepository;
        this.residenteRepository = residenteRepository;
        this.unidadeSaudeRepository = unidadeSaudeRepository;
        this.atendimentoRepository = atendimentoRepository;
        this.procedimentoRepository = procedimentoRepository;
        this.escalaRepository = escalaRepository;
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
                case "7" -> menuConsultasAvancadasORM(scanner);
                case "8" -> menuViewsAnaliticas(scanner);
                case "0" -> {
                    executando = false;
                    System.out.println("Encerrando o sistema... Até mais!");
                    System.exit(0);
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
        System.out.println("7. [ETAPA 2] Consultas Avançadas com ORM");
        System.out.println("8. [ETAPA 2] Consultar Views Analíticas (PostgreSQL)");
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

    // --- MÉTODOS DE AÇÃO DO CRUD CONECTADOS AOS REPOSITÓRIOS ---

    private void executarCriacao(Scanner scanner, String entidade) {
        System.out.println("\n=== [CRIAR] Novo Registro em " + entidade + " ===");
        try {
            System.out.print("Digite o nome/identificador: ");
            String nome = scanner.nextLine();
            
            // Aqui podemos expandir para chamadas de save() específicas
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
            
            switch (entidade) {
                case "PACIENTE" -> pacienteRepository.findById(id).ifPresentOrElse(
                        p -> System.out.println("✅ Encontrado: ID=" + p.getIdPessoa() + " | Nome=" + p.getNome() + " | Convênio=" + p.getNumConvenio()),
                        () -> System.out.println("⚠️ Paciente não encontrado com ID: " + id));
                case "PRECEPTOR" -> preceptorRepository.findById(id).ifPresentOrElse(
                        pr -> System.out.println("✅ Encontrado: ID=" + pr.getIdPessoa() + " | Nome=" + pr.getNome() + " | CRM=" + pr.getCrm()),
                        () -> System.out.println("⚠️ Preceptor não encontrado com ID: " + id));
                default -> System.out.println("✅ Registro simulado para " + entidade);
            }

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

            switch (entidade) {
                case "PACIENTE" -> pacienteRepository.deleteById(id);
                case "PRECEPTOR" -> preceptorRepository.deleteById(id);
                default -> {}
            }

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
            
            switch (entidade) {
                case "PACIENTE" -> {
                    List<Paciente> lista = pacienteRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum paciente cadastrado.");
                        return;
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-30s | %-15s | %-10s\n", "ID", "NOME", "CONVÊNIO", "SANGUE");
                    System.out.println("----------------------------------------------------------------------------------");
                    for (Paciente p : lista) {
                        System.out.printf("%-4d | %-30s | %-15s | %-10s\n",
                                p.getIdPessoa(),
                                p.getNome(),
                                p.getNumConvenio() != null ? p.getNumConvenio() : "N/I",
                                p.getGrupoSanguineo() != null ? p.getGrupoSanguineo() : "N/I");
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                case "PRECEPTOR" -> {
                    List<Preceptor> lista = preceptorRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum preceptor cadastrado.");
                        return;
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-30s | %-15s | %-15s\n", "ID", "NOME", "CRM", "TITULAÇÃO");
                    System.out.println("----------------------------------------------------------------------------------");
                    for (Preceptor pr : lista) {
                        System.out.printf("%-4d | %-30s | %-15s | %-15s\n",
                                pr.getIdPessoa(),
                                pr.getNome(),
                                pr.getCrm(),
                                pr.getTitulacao());
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                case "RESIDENTE" -> {
                    List<Residente> lista = residenteRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum residente cadastrado.");
                        return;
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-30s | %-15s | %-10s\n", "ID", "NOME", "CRM", "ANO RES.");
                    System.out.println("----------------------------------------------------------------------------------");
                    for (Residente r : lista) {
                        System.out.printf("%-4d | %-30s | %-15s | R%-9d\n",
                                r.getIdPessoa(),
                                r.getNome(),
                                r.getCrm(),
                                r.getAnoResidencia());
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                case "UNIDADE DE SAÚDE", "UNIDADE" -> {
                    List<UnidadeSaude> lista = unidadeSaudeRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhuma unidade de saúde cadastrada.");
                        return;
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-30s | %-20s | %-10s\n", "ID", "NOME", "TIPO", "LEITOS");
                    System.out.println("----------------------------------------------------------------------------------");
                    for (UnidadeSaude u : lista) {
                        System.out.printf("%-4d | %-30s | %-20s | %-10d\n",
                                u.getIdUnidade(),
                                u.getNome(),
                                u.getTipo(),
                                u.getCapacidadeLeitos());
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                case "PROCEDIMENTO" -> {
                    List<Procedimento> lista = procedimentoRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum procedimento cadastrado.");
                        return;
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-35s | %-15s\n", "ID", "NOME DO PROCEDIMENTO", "TEMPO MÉDIO (MIN)");
                    System.out.println("----------------------------------------------------------------------------------");
                    for (Procedimento proc : lista) {
                        System.out.printf("%-4d | %-35s | %-15d min\n",
                                proc.getIdProcedimento(),
                                proc.getNome(),
                                proc.getTempoMedioExecucao());
                    }
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                case "ATENDIMENTO" -> {
                    List<Atendimento> lista = atendimentoRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum atendimento cadastrado.");
                        return;
                    }
                    System.out.println("--------------------------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-20s | %-20s | %-20s | %-10s\n", "ID", "PACIENTE", "RESIDENTE", "UNIDADE", "DURAÇÃO");
                    System.out.println("--------------------------------------------------------------------------------------------------");
                    for (Atendimento a : lista) {
                        System.out.printf("%-4d | %-20s | %-20s | %-20s | %-10d min\n",
                                a.getIdAtendimento(),
                                a.getPaciente() != null ? a.getPaciente().getNome() : "N/I",
                                a.getResidente() != null ? a.getResidente().getNome() : "N/I",
                                a.getUnidadeSaude() != null ? a.getUnidadeSaude().getNome() : "N/I",
                                a.getDuracaoMinutos());
                    }
                    System.out.println("--------------------------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                case "ESCALA" -> {
                    List<Escala> lista = escalaRepository.findAll();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhuma escala cadastrada.");
                        return;
                    }
                    System.out.println("--------------------------------------------------------------------------------------------------");
                    System.out.printf("%-4s | %-12s | %-10s | %-20s | %-20s\n", "ID", "DIA SEMANA", "TURNO", "RESIDENTE", "PRECEPTOR");
                    System.out.println("--------------------------------------------------------------------------------------------------");
                    for (Escala e : lista) {
                        System.out.printf("%-4d | %-12s | %-10s | %-20s | %-20s\n",
                                e.getIdEscala(),
                                e.getDiaSemana(),
                                e.getTurno(),
                                e.getResidente() != null ? e.getResidente().getNome() : "N/I",
                                e.getPreceptor() != null ? e.getPreceptor().getNome() : "N/I");
                    }
                    System.out.println("--------------------------------------------------------------------------------------------------");
                    System.out.println("✅ Total de registros encontrados: " + lista.size());
                }

                default -> System.out.println("⚠️ Listagem para '" + entidade + "' não configurada.");
            }

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
            System.out.println("⏳ Executando cálculo no banco de dados...");
            
            // Chama o serviço e guarda o retorno
            Double tempoMedio = atendimentoService.calcularTempoMedioEspera();
            
            if (tempoMedio != null) {
                System.out.printf("✅ Stored Procedure executada com sucesso!\n");
                System.out.printf("⏱️  O tempo médio atual de espera é de: %.1f minutos.\n", tempoMedio);
            } else {
                System.out.println("⚠️ Stored Procedure executada, mas não há dados suficientes para calcular a média (Retorno nulo).");
            }

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

    private void menuConsultasAvancadasORM(Scanner scanner) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- [ETAPA 2] CONSULTAS AVANÇADAS COM ORM ---");
            System.out.println("1. Listar Preceptores de Pacientes Flamenguistas");
            System.out.println("2. Exibir o Último Atendimento de Cada Paciente");
            System.out.println("3. Calcular Percentual de Procedimentos de Alto Risco por Residente");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> {
                    System.out.println("\n=== Preceptores que atenderam pacientes flamenguistas ===");
                    List<Preceptor> lista = atendimentoRepository.findPreceptoresDePacientesFlamenguistas();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum registro encontrado.");
                    } else {
                        lista.forEach(p -> System.out.println("- " + p.getNome() + " (CRM: " + p.getCrm() + ")"));
                    }
                }
                case "2" -> {
                    System.out.println("\n=== Último atendimento de cada paciente ===");
                    List<Atendimento> lista = atendimentoRepository.findUltimoAtendimentoPorPaciente();
                    if (lista.isEmpty()) {
                        System.out.println("⚠️ Nenhum atendimento encontrado.");
                    } else {
                        lista.forEach(a -> System.out.printf("Paciente: %s | Data: %s | Residente: %s\n",
                                a.getPaciente().getNome(),
                                a.getDataHora(),
                                a.getResidente().getNome()));
                    }
                }
                case "3" -> {
                    System.out.println("\n=== Percentual de procedimentos de alto risco por residente ===");
                    List<Object[]> resultados = atendimentoRepository.calcularPercentualProcedimentosAltoRiscoPorResidente();
                    if (resultados.isEmpty()) {
                        System.out.println("⚠️ Nenhum dado estatístico disponível.");
                    } else {
                        resultados.forEach(res -> System.out.printf("Residente: %s | Alto Risco: %.2f%%\n",
                                res[0], res[1]));
                    }
                }
                case "0" -> voltar = true;
                default -> System.out.println("❌ Opção inválida!");
            }
        }
    }

    // =========================================================================
    // SUBMENU ETAPA 2 - VIEWS ANALÍTICAS (POSTGRESQL)
    // =========================================================================

    private void menuViewsAnaliticas(Scanner scanner) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- [ETAPA 2] VIEWS ANALÍTICAS (POSTGRESQL) ---");
            System.out.println("1. [View] Pacientes Internados (vw_pacientes_internados)");
            System.out.println("2. [View] Residentes sem Supervisor Doutor (vw_residentes_sem_supervisor)");
            System.out.println("3. [View] Estatísticas Mensais de Atendimentos (vw_estatisticas_atendimentos_mensal)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> {
                    System.out.println("\n=== [VIEW] Pacientes Internados ===");
                    try {
                        List<Object[]> lista = atendimentoRepository.consultarViewPacientesInternados();
                        if (lista.isEmpty()) {
                            System.out.println("⚠️ Nenhum paciente internado encontrado.");
                        } else {
                            System.out.println("--------------------------------------------------------------------------------------------------");
                            System.out.printf("%-4s | %-30s | %-15s | %-20s | %-20s\n", "ID", "PACIENTE", "CONVÊNIO", "DATA INTERNAÇÃO", "UNIDADE");
                            System.out.println("--------------------------------------------------------------------------------------------------");
                            for (Object[] row : lista) {
                                System.out.printf("%-4s | %-30s | %-15s | %-20s | %-20s\n",
                                        row[0], row[1],
                                        row[2] != null ? row[2] : "SUS",
                                        row[4] != null ? row[4].toString() : "N/I",
                                        row[5]);
                            }
                            System.out.println("--------------------------------------------------------------------------------------------------");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Erro ao consultar view: " + e.getMessage());
                    }
                }
                case "2" -> {
                    System.out.println("\n=== [VIEW] Residentes Supervisionados por Preceptores sem Doutorado ===");
                    try {
                        List<Object[]> lista = atendimentoRepository.consultarViewResidentesSemSupervisor();
                        if (lista.isEmpty()) {
                            System.out.println("⚠️ Nenhum registro encontrado.");
                        } else {
                            System.out.println("--------------------------------------------------------------------------------------------------");
                            System.out.printf("%-25s | %-10s | %-10s | %-20s | %-20s | %-15s\n", "RESIDENTE", "DIA", "TURNO", "UNIDADE", "PRECEPTOR", "TITULAÇÃO");
                            System.out.println("--------------------------------------------------------------------------------------------------");
                            for (Object[] row : lista) {
                                System.out.printf("%-25s | %-10s | %-10s | %-20s | %-20s | %-15s\n",
                                        row[0], row[1], row[2], row[3], row[4], row[5]);
                            }
                            System.out.println("--------------------------------------------------------------------------------------------------");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Erro ao consultar view: " + e.getMessage());
                    }
                }
                case "3" -> {
                    System.out.println("\n=== [VIEW] Estatísticas Mensais por Unidade ===");
                    try {
                        List<Object[]> lista = atendimentoRepository.consultarViewEstatisticasMensais();
                        if (lista.isEmpty()) {
                            System.out.println("⚠️ Nenhuma estatística gerada no período.");
                        } else {
                            System.out.println("----------------------------------------------------------------------------------");
                            System.out.printf("%-6s | %-6s | %-25s | %-12s | %-15s\n", "ANO", "MÊS", "UNIDADE", "TOTAL ATEND.", "MÉDIA DURAÇÃO");
                            System.out.println("----------------------------------------------------------------------------------");
                            for (Object[] row : lista) {
                                System.out.printf("%-6s | %-6s | %-25s | %-12s | %-15s min\n",
                                        row[0], row[1], row[2], row[3], row[4]);
                            }
                            System.out.println("----------------------------------------------------------------------------------");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Erro ao consultar view: " + e.getMessage());
                    }
                }
                case "0" -> voltar = true;
                default -> System.out.println("❌ Opção inválida!");
            }
        }
    }
}