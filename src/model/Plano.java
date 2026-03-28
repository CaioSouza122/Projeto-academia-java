package model;

/**
 * Classe de modelo Plano representa um plano de assinatura da academia
 * 
 * Exemplo: Plano Ouro - R$ 100.00 por mês com duração de 1 mês
 * 
 * Atributos:
 * - id: Identificador único do plano
 * - nome: Nome do plano (ex: "Plano Básico", "Plano Premium")
 * - valor: Valor mensal do plano em reais
 * - descricao: Descrição do que o plano inclui
 * - duracaoMeses: Duração do plano em meses
 */
public class Plano {

    private int id;
    private String nome;
    private double valor;
    private String descricao;
    private int duracaoMeses;

    // Construtor sem parâmetros (construtor padrão)
    public Plano() {}

    // Construtor com todos os parâmetros
    public Plano(int id, String nome, double valor, String descricao, int duracaoMeses) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
        this.duracaoMeses = duracaoMeses;
    }

    // Getters e Setters (métodos de acesso aos atributos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    /**
     * Método toString - Retorna representação em texto formatada
     * Exibe: "[id] Nome - R$ valor (X mês(es)) | Descrição"
     */
    @Override
    public String toString() {
        return String.format("[%d] %s - R$ %.2f (%d mes(es)) | %s", id, nome, valor, duracaoMeses, descricao);
    }
}
