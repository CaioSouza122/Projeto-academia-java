package model;

/**
 * Classe de modelo Aluno representa um aluno/cliente da academia
 * 
 * Atributos:
 * - id: Identificador único do aluno
 * - nome: Nome completo do aluno
 * - cpf: CPF (documento de identificação)
 * - email: Email para contato e login
 * - senha: Senha encriptada para acesso ao sistema
 * - telefone: Número de telefone para contato
 * - professorId: ID do professor designado (0 se não tiver)
 * - planoId: ID do plano de assinatura (0 se não tiver)
 * - ativo: Flag indicando se o aluno está ativo ou deletado (soft delete)
 */
public class Aluno {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String telefone;
    private int professorId;
    private int planoId;
    private boolean ativo;

    // Construtor sem parâmetros (construtor padrão)
    public Aluno() {}

    // Construtor com todos os parâmetros
    public Aluno(int id, String nome, String cpf, String email, String senha,
                 String telefone, int professorId, int planoId, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.professorId = professorId;
        this.planoId = planoId;
        this.ativo = ativo;
    }

    // Getters e Setters (métodos de acesso aos atributos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public int getPlanoId() { return planoId; }
    public void setPlanoId(int planoId) { this.planoId = planoId; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    /**
     * Método toString - Retorna representação em texto formatada
     * Exibe: "[id] Nome | CPF: xxx | Email: xxx | Ativo: Sim/Não"
     */
    @Override
    public String toString() {
        return String.format("[%d] %s | CPF: %s | Email: %s | Ativo: %s", id, nome, cpf, email, ativo ? "Sim" : "Não");
    }
}
