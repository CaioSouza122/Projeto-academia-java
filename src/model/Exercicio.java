package model;

/**
 * Classe de modelo Exercicio representa um exercício dentro de um treino
 * 
 * Exemplo: Flexão de braço com 3 séries de 12 repetições e 60 segundos de descanso
 * 
 * Atributos:
 * - id: Identificador único do exercício
 * - treinoId: ID do treino a que este exercício pertence
 * - nome: Nome/descrição do exercício
 * - series: Número de séries a fazer
 * - repeticoes: Número de repetições por série
 * - descansoSeg: Tempo de descanso em segundos entre séries
 */
public class Exercicio {

    private int id;
    private int treinoId;
    private String nome;
    private int series;
    private int repeticoes;
    private int descansoSeg;

    // Construtor sem parâmetros (construtor padrão)
    public Exercicio() {}

    // Construtor com todos os parâmetros
    public Exercicio(int id, int treinoId, String nome, int series, int repeticoes, int descansoSeg) {
        this.id = id;
        this.treinoId = treinoId;
        this.nome = nome;
        this.series = series;
        this.repeticoes = repeticoes;
        this.descansoSeg = descansoSeg;
    }

    // Getters e Setters (métodos de acesso aos atributos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTreinoId() { return treinoId; }
    public void setTreinoId(int treinoId) { this.treinoId = treinoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }

    public int getRepeticoes() { return repeticoes; }
    public void setRepeticoes(int repeticoes) { this.repeticoes = repeticoes; }

    public int getDescansoSeg() { return descansoSeg; }
    public void setDescansoSeg(int descansoSeg) { this.descansoSeg = descansoSeg; }

    /**
     * Método toString - Retorna representação em texto formatada
     * Exibe: "  - nome_exercicio: seriesxrepeticoes (descanso: XXs)"
     */
    @Override
    public String toString() {
        return String.format("  - %s: %dx%d (descanso: %ds)", nome, series, repeticoes, descansoSeg);
    }
}
