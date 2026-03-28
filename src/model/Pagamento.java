package model;

import java.time.LocalDate;

/**
 * Classe de modelo Pagamento representa um registro de pagamento de plano por um aluno
 * 
 * Atributos:
 * - id: Identificador único do pagamento
 * - alunoId: ID do aluno que fez o pagamento
 * - planoId: ID do plano que foi pago
 * - dataPagamento: Data em que o pagamento foi feito
 * - dataVencimento: Data de vencimento do plano/próximo pagamento
 * - status: Status do pagamento ("pago", "pendente", "vencido")
 */
public class Pagamento {

    private int id;
    private int alunoId;
    private int planoId;
    private LocalDate dataPagamento;
    private LocalDate dataVencimento;
    private String status; // "pago", "pendente", "vencido"

    // Construtor sem parâmetros (construtor padrão)
    public Pagamento() {}

    // Construtor com todos os parâmetros
    public Pagamento(int id, int alunoId, int planoId, LocalDate dataPagamento, LocalDate dataVencimento, String status) {
        this.id = id;
        this.alunoId = alunoId;
        this.planoId = planoId;
        this.dataPagamento = dataPagamento;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }

    // Getters e Setters (métodos de acesso aos atributos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAlunoId() { return alunoId; }
    public void setAlunoId(int alunoId) { this.alunoId = alunoId; }

    public int getPlanoId() { return planoId; }
    public void setPlanoId(int planoId) { this.planoId = planoId; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Método toString - Retorna representação em texto formatada
     * Exibe informações do pagamento de forma legível
     */
    @Override
    public String toString() {
        return String.format("Pagamento #%d | Status: %s | Vencimento: %s", id, status, dataVencimento);
    }
}
