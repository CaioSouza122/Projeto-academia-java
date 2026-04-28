# GymFlow — Sistema de Gerenciamento de Academia

> Projeto Java para gerenciamento de academia, desenvolvido como projeto de faculdade.

Sistema de console com três perfis de acesso: **Aluno**, **Professor** e **Recepcionista**, utilizando Java com PostgreSQL via JDBC.

---

## 📁 Estrutura do Projeto

```
Projeto-academia-java/
├── src/
│   ├── app/               # Ponto de entrada da aplicação
│   │   └── GymFlowSystem.java
│   ├── connection/        # Gerenciamento da conexão PostgreSQL
│   │   └── Conexao.java
│   ├── model/             # Classes de modelo (entidades)
│   │   ├── Aluno.java
│   │   ├── Professor.java
│   │   ├── Recepcionista.java
│   │   ├── Treino.java
│   │   ├── Exercicio.java
│   │   ├── Pagamento.java
│   │   └── Plano.java
│   ├── dao/               # Acesso ao banco de dados (DAO pattern)
│   │   ├── AlunoDAO.java
│   │   ├── ProfessorDAO.java
│   │   ├── PagamentoDAO.java
│   │   ├── PlanoDAO.java
│   │   └── TreinoDAO.java
│   └── view/              # Menus de console (interface com usuário)
│       ├── MenuLogin.java
│       ├── MenuAluno.java
│       ├── MenuProfessor.java
│       └── MenuRecepcionista.java
├── libs/
│   └── postgresql-42.7.10.jar  # Driver JDBC do PostgreSQL
├── banco.sql              # Script de criação do banco de dados
└── .vscode/
    └── settings.json      # Configura o classpath do JDBC no VS Code
```

---

## 🛠️ Tecnologias

| Tecnologia | Descrição |
|---|---|
| Java 17+ | Linguagem principal |
| PostgreSQL | Banco de dados relacional |
| JDBC | Conexão Java ↔ PostgreSQL |
| VS Code | IDE com Extension Pack for Java |

---

## ⚙️ Configuração do Banco de Dados

### 1. Crie o banco no PostgreSQL

```sql
CREATE DATABASE gymflow;
```

### 2. Execute o script SQL

No terminal do psql ou no pgAdmin, rode:

```bash
\i banco.sql
```

Ou simplesmente abra o arquivo `banco.sql` no pgAdmin e execute.

### 3. Configure as credenciais

Edite o arquivo `src/connection/Conexao.java` com os dados do seu PostgreSQL:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/gymflow";
private static final String USUARIO = "postgres";
private static final String SENHA   = "sua_senha_aqui";
```

> ⚠️ **Atenção:** Nunca suba senhas reais para o GitHub. Mantenha as credenciais locais.

---

## 🚀 Como Executar

1. Certifique-se que o PostgreSQL está rodando e o banco foi criado conforme acima.
2. Abra a pasta do projeto no **VS Code**.
3. Instale a extensão **Extension Pack for Java** se ainda não tiver.
4. O arquivo `.vscode/settings.json` já configura automaticamente o driver JDBC da pasta `libs/`.
5. Abra `src/app/GymFlowSystem.java` e clique em **▶ Run**.

---

## 🔐 Login Inicial

O script `banco.sql` já insere uma conta de recepcionista padrão:

| Campo | Valor |
|---|---|
| Email | `recepcao@gymflow.com` |
| Senha | `1234` |

> Lembre-se de trocar a senha após o primeiro acesso em ambiente de produção.

---

## 👥 Perfis do Sistema

### 🧑‍🎓 Aluno
- Visualizar seus treinos e exercícios
- Consultar pagamentos e status do plano
- Atualizar dados do perfil

### 🏋️ Professor
- Cadastrar e gerenciar treinos dos alunos
- Adicionar exercícios aos treinos
- Visualizar lista de alunos vinculados

### 🖥️ Recepcionista
- Cadastrar novos alunos
- Registrar pagamentos
- Atribuir professores e planos aos alunos
- Cadastrar professores

---

## 🗄️ Modelo do Banco de Dados

```
planos ──────────────────────────────────────────────────────────┐
  id, nome, valor, descricao, duracao_meses                       │
                                                                  │
usuarios ◄── professor_id (auto-ref) ── plano_id ───────────────►┘
  id, nome, cpf, email, senha, tipo, professor_id, plano_id, ativo

pagamentos ──► usuarios (aluno_id) ──► planos (plano_id)
  id, aluno_id, plano_id, data_pagamento, data_vencimento, status

treinos ──► usuarios (aluno_id, professor_id)
  id, nome, aluno_id, professor_id, descricao, data_criacao

exercicios ──► treinos (treino_id)
  id, treino_id, nome, series, repeticoes, descanso_seg
```
