package model;

/**
 * Classe de modelo Professor representa um professor/personal trainer da academia
 * 
 * Atributos:
 * - id: Identificador único do professor
 * - nome: Nome completo do professor
 * - cpf: CPF (documento de identificação) do professor
 * - email: Email para contato
 * - senha: Senha encriptada para login
 */
public class Professor {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    // Construtor sem parâmetros (construtor padrão)
    public Professor() {}

    // Construtor com todos os parâmetros
    public Professor(int id, String nome, String cpf, String email, String senha) {
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
     * Exibe: "[id] Prof. Nome | Email"
     */
    @Override
    public String toString() {
        return String.format("[%d] Prof. %s | %s", id, nome, email);
    }
}
