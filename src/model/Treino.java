package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de modelo Treino representa um plano de treino de um aluno
 * 
 * Exemplo: Treino A - Peito e Tríceps contendo 5 exercícios
 * 
 * Atributos:
 * - id: Identificador único do treino
 * - nome: Nome/identificação do treino (ex: "Treino A", "Treino B")
 * - alunoId: ID do aluno a quem o treino pertence
 * - professorId: ID do professor que criou o treino
 * - descricao: Descrição do treino
 * - exercicios: Lista de exercícios que compõem este treino
 */
public class Treino {

    private int id;
    private String nome;
    private int alunoId;
    private int professorId;
    private String descricao;
    private List<Exercicio> exercicios;

    // Construtor sem parâmetros - inicializa lista de exercícios vazia
    public Treino() {
        this.exercicios = new ArrayList<>();
    }

    // Construtor com parâmetros principais (exercícios são carregados depois)
    public Treino(int id, String nome, int alunoId, int professorId, String descricao) {
        this.id = id;
        this.nome = nome;
        this.alunoId = alunoId;
        this.professorId = professorId;
        this.descricao = descricao;
        this.exercicios = new ArrayList<>();
    }

    // Getters e Setters (métodos de acesso aos atributos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getAlunoId() { return alunoId; }
    public void setAlunoId(int alunoId) { this.alunoId = alunoId; }

    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Exercicio> getExercicios() { return exercicios; }
    public void setExercicios(List<Exercicio> exercicios) { this.exercicios = exercicios; }
    
    /**
     * Método adicionarExercicio - Adiciona um exercício à lista de exercícios do treino
     * @param e Exercício a ser adicionado
     */
    public void adicionarExercicio(Exercicio e) { this.exercicios.add(e); }

    /**
     * Método toString - Retorna representação em texto formatada
     * Exibe o nome e descrição do treino, seguido por lista de exercícios
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Cabeçalho do treino
        sb.append(String.format("Treino: %s | %s\n", nome, descricao));
        // Lista cada exercício do treino
        for (Exercicio ex : exercicios) {
            sb.append(ex.toString()).append("\n");
        }
        return sb.toString();
    }
}
