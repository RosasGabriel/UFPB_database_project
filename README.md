# Sistema de Gestão Hospitalar (UFPB) - Etapa 1

Este repositório contém a especificação e a implementação da primeira etapa do projeto de banco de dados para o Sistema de Gestão Hospitalar, disciplina de Banco de Dados da UFPB.

O projeto contempla o Modelo Conceitual (DER), o Modelo Relacional Normalizado e os scripts de automação física em SQL puro para o SGDB **PostgreSQL**.

---

## 🛠️ Guia de Instalação e Configuração do Ambiente

Escolha a seção adequada para o seu sistema operacional para instalar o **PostgreSQL** e o **pgAdmin 4**.

### 🐧 Opção 1: Instalação no Linux (Linux Mint / Ubuntu / Debian-based)

No terminal do seu Linux, execute os passos abaixo:

**1.Atualizar os repositórios do sistema:**
```bash
sudo apt update && sudo apt upgrade -y
```

**2.Instalar o servidor PostgreSQL e utilitários adicionais:**
```bash
sudo apt install postgresql postgresql-contrib -y
```

**3.Verificar se o serviço está ativo e rodando:**
```bash
sudo systemctl status postgresql
```
(Pressione **q** para sair do status).

**4.Definir/Resetar a senha do superusuário postgres no banco:**
Por padrão, o Linux usa a autenticação do sistema (peer). Para liberar o acesso via senha no pgAdmin, acesse o terminal do banco:
```bash
sudo -u postgres psql
```
Dentro do prompt do PostgreSQL (postgres=#), execute o comando de alteração de senha (substitua pela sua senha de preferência):
```bash
ALTER USER postgres WITH PASSWORD 'sua_senha_aqui';
```
Digite **\q** e pressione **Enter** para sair.

**4.Instalar o pgAdmin 4:**
Siga o guia oficial para adicionar o repositório do pgAdmin (APT) e instale via:
```bash
sudo apt install pgadmin4 -y
```

### 🪟 Opção 2: Instalação no Windows
**1.Download do Instalador:**
Acesse a página oficial do PostgreSQL (https://www.postgresql.org/download/windows/) e baixe o instalador da versão mais recente (ex: 15 ou 16).

**2.Execução do Assistente:**

Dê um duplo clique no instalador .exe.

Avance pelas telas de diretório padrão.

Na tela de seleção de componentes, certifique-se de marcar: PostgreSQL Server, pgAdmin 4 e Command Line Tools.

**3.Configuração de Senha:**
Durante a instalação, o assistente solicitará uma senha para o usuário master postgres. Anote esta senha, pois ela será exigida para conectar o pgAdmin ao servidor local.

**4.Porta Padrão:**
Mantenha a porta padrão recomendada pelo instalador: 5432.

**5.Finalização:**
Conclua o assistente. O PostgreSQL passará a rodar automaticamente como um Serviço do Windows em segundo plano.

### 💻 Como Executar os Scripts no pgAdmin 4
Com o ambiente instalado, siga a ordem estrita descrita abaixo para montar o banco de dados do hospital:

**1.Criação do Banco de Dados:**
   Crie um banco de dados vazio chamado `SistemaGestaoHospitalar`.

**2.Criação das Tabelas:**
   Execute o script `schemas.sql` para gerar toda a estrutura de tabelas, chaves primárias/estrangeiras e restrições.

**3.Carga de Dados de Teste:**
   Execute o script `seeds.sql` para povoar as tabelas com os registros de teste mínimos.

**4.Execução das Consultas e Relatórios:**
   O arquivo `queries.sql` contém todos os testes de CRUD e consultas analíticas exigidos na especificação.