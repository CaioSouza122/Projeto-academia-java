package model;

/**
 * Classe de modelo Recepcionista representa um recepcionista/funcionário da academia
 * 
 * Responsabilidades: Registrar alunos, gerenciar pagamentos, atribuir professores
 * 
 * Atributos:
 * - id: Identificador único do recepcionista
 * - nome: Nome completo
 * - cpf: CPF (documento de identificação)
 * - email: Email para contato
 * - senha: Senha encriptada para login
 */
public class Recepcionista {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    // Construtor sem parâmetros (construtor padrão)
    public Recepcionista() {}

    // Construtor com todos os parâmetros
    public Recepcionista(int id, String nome, String cpf, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
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

    /**
     * Método toString - Retorna representação em texto formatada
     * Exibe: "[id] Recepcionista: Nome | Email"
     */
    @Override
    public String toString() {
        return String.format("[%d] Recepcionista: %s | %s", id, nome, email);
    }
}
