# GymFlow - Sistema de Gerenciamento de Academia

Projeto Java para gerenciamento de academia, desenvolvido para faculdade.

## 📁 Estrutura do Projeto (Refatorada)

```
src/
├── app/               # Classe principal e inicialização
│   └── GymFlowSystem.java
├── connection/        # Gerenciamento da conexão PostgreSQL
│   └── Conexao.java
├── model/             # Classes de modelo (Aluno, Professor, Treino, etc)
├── dao/               # Acesso ao banco de dados (Data Access Objects)
├── view/              # Menus de console (Interface com usuário)
└── libs/              # Drivers e bibliotecas externas (JDBC)
```

## 🛠️ Tecnologias

- Java
- PostgreSQL (via JDBC)
- VS Code (Extension Pack for Java)

## ⚙️ Configuração do Banco de Dados

1. Execute o script `banco.sql` no seu PostgreSQL.
2. Edite `src/connection/Conexao.java` com suas credenciais:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/NomeBanco";
private static final String USUARIO = "postgres";
private static final String SENHA   = "root";
```

## 🚀 Como Executar

1. Baixe o driver JDBC do PostgreSQL e coloque na pasta `libs/`.
2. O VS Code já está pré-configurado via `.vscode/settings.json` para reconhecer a pasta `libs/`.
3. Abra `src/app/GymFlowSystem.java` e clique em **Run**.

**Login Inicial (Recepção):**
- **Email:** `recepcao@gymflow.com`
- **Senha:** `1234`
