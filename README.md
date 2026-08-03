# 🏥 Sistema de Gestão Hospitalar - Hospital Universitário Dra. Yuska Maritan Brito

Este repositório contém a especificação e a implementação completa (**Etapa 1** e **Etapa 2**) do sistema de gestão hospitalar para o Hospital Universitário Dra. Yuska Maritan Brito, desenvolvido para a disciplina de Banco de Dados da **Universidade Federal da Paraíba (UFPB)**.

O projeto abrange desde a modelagem relacional avançada até a automação de regras de negócio via **Stored Procedures**, **Triggers**, **Views** no **PostgreSQL**, além da integração de uma aplicação CLI interativa em **Java 21 / Spring Boot** com **Spring Data JPA (Hibernate)**.

---

## 📐 Estrutura e Modelagem do Banco de Dados

O modelo de dados contempla a gestão de **Pessoas** (com especialização/herança em **Pacientes** e **Profissionais**, estes divididos em **Preceptores** e **Residentes**), **Unidades de Saúde**, **Atendimentos**, **Procedimentos Realizados**, **Escalas de Plantão** e registros de **Auditoria**.

### 🧬 Diagrama e Documentação
* **DER e Modelo Relacional:** Disponíveis no arquivo `docs/Modelo_Conceitual_e_Relacional.pdf` (com justificativas de cardinalidades e evidência da normalização em **3FN/BCNF**).

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem & Framework:** Java 21 / Spring Boot 4.1.0
* **ORM & Persistência:** Hibernate / Spring Data JPA
* **Gerenciador de Dependências:** Maven
* **Banco de Dados:** PostgreSQL 16.14
* **Interface:** CLI Interativa (Terminal)

---

## 💻 Guia de Instalação e Configuração do Ambiente

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
* **JDK 21** ou superior
* **Apache Maven 3.8+**
* **PostgreSQL 16+**

### 2. Configuração do PostgreSQL

No terminal Linux/Windows, acesse o prompt do PostgreSQL e crie o banco de dados do projeto:

```sql
CREATE DATABASE postgres; -- Ou utilize o banco padrão 'postgres'
```

Certifique-se de ajustar as credenciais de acesso no arquivo src/main/resources/application.properties se necessário:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
```
## 🚀 Como Executar o Projeto

### 1. Clone o repositório:

```
git clone https://github.com/RosasGabriel/UFPB_database_project.git
cd hospital-management
```

### 2. Compile e execute a aplicação usando Maven:

```
mvn spring-boot:run
```
O Spring Boot iniciará, aplicará os scripts DDL/DML automaticamente e abrirá a CLI Interativa no terminal.

## 📑 Funcionalidades Implementadas
### Etapa 1: Fundamentos e Modelo Relacional
* **CRUDs Completos:**

Gestão de Pacientes, Preceptores, Residentes, Unidades e Atendimentos.

Listagem de atendimentos por paciente (ordenados por data).

Listagem e remoção condicional de procedimentos realizados (flag pode_remover).

* **Consultas Analíticas:**

Ranking de residentes por volume de atendimentos.

Preceptores com mais de 5 supervisões mensais.

Quantidade de plantões escalados por unidade/residente no mês corrente.

Pacientes que nunca realizaram procedimentos de risco 'ALTO'.

### Etapa 2: Recursos Avançados e ORM
**1. Stored Procedures**

sp_registrar_atendimento_completo: Transação atômica que cadastra atendimento e lista de procedimentos via parâmetro JSON.

sp_calcular_tempo_medio_espera: Calcula a média de tempo até o início do primeiro procedimento por unidade.

sp_reajustar_escala: Remaneja escalas de residentes evitando sobreposições de horários.

**2. Triggers**

trg_check_sobreposicao_escala: Impede alocação dupla do mesmo residente no mesmo dia/turno em unidades diferentes (BEFORE INSERT/UPDATE).

trg_audita_atendimento: Grava logs de alterações na tabela AUDITORIA_ATENDIMENTO com snapshots JSONB dos dados antigos e novos (AFTER INSERT/UPDATE/DELETE).

trg_atualiza_media_procedimentos: Atualiza a média de tempo de execução no cadastro principal do procedimento (AFTER INSERT).

**3. Views Analíticas**

vw_pacientes_internados: Pacientes atualmente internados sem data de saída.

vw_residentes_sem_supervisor: Residentes em plantões supervisionados por preceptores sem doutorado.

vw_estatisticas_atendimentos_mensal: Agregação mensal com total de atendimentos e médias.

**4. Consultas Avançadas com ORM (Hibernate / JPQL)**

Preceptores de Flamenguistas: Identifica preceptores que supervisionaram residentes ao atenderem pacientes torcedores do Flamengo (is_flamengo = TRUE).

Último Atendimento: Exibe o último atendimento de cada paciente com os detalhes dos procedimentos realizados.

Percentual de Alto Risco: Calcula a porcentagem de procedimentos executados com tempo médio superior a 60 minutos por residente.