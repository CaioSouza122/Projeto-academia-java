# GymFlow - Sistema de Gerenciamento de Academia

Projeto Java para gerenciamento de academia, desenvolvido para faculdade.

## 📁 Estrutura do Projeto

```
src/
├── Conection/
│   └── Conexao.java       # Gerencia a conexão com o banco de dados PostgreSQL
└── System/
    ├── Aluno.java          # Modelo de dados do aluno
    └── GymFlowSystem.java  # Classe principal (entry point)
```

## 🛠️ Tecnologias

- Java
- PostgreSQL (via JDBC)

## ⚙️ Configuração do Banco de Dados

Edite o arquivo `src/Conection/Conexao.java` e altere as constantes:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/NomeBanco";
private static final String USUARIO = "postgres";
private static final String SENHA   = "root";
```

## 🚀 Como executar

1. Configure o PostgreSQL e crie o banco de dados
2. Ajuste as credenciais em `Conexao.java`
3. Adicione o driver JDBC do PostgreSQL ao classpath
4. Compile e execute `GymFlowSystem.java`
