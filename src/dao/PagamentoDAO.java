package dao;

import connection.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Pagamento;

/**
 * Classe PagamentoDAO responsável por gerenciar todas as operações de acesso a dados
 * relacionadas a pagamentos de planos dos alunos
 * 
 * Métodos:
 * - registrar: Registra um novo pagamento e atualiza o plano do aluno
 * - verificarUltimoPagamento: Busca o último pagamento de um aluno
 * - buscarPorAluno: Retorna histórico de pagamentos de um aluno
 * - atualizarPlanoAluno: Atualiza o plano na tabela de usuários (interno)
 * - mapear: Converte ResultSet em objeto Pagamento (interno)
 */
public class PagamentoDAO {

    /**
     * Método registrar - Registra um novo pagamento e atualiza o plano do aluno
     * 
     * @param pag Objeto Pagamento com os dados a serem inseridos
     * @return true se registrou com sucesso, false caso contrário
     * 
     * Processo:
     * 1. Insere o pagamento na tabela pagamentos
     * 2. Atualiza o plano_id do aluno na tabela usuarios
     * 3. Recupera o ID gerado automaticamente
     */
    public boolean registrar(Pagamento pag) {
        // SQL INSERT para registrar novo pagamento
        String sql = "INSERT INTO pagamentos (aluno_id, plano_id, data_pagamento, data_vencimento, status) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os valores dos parâmetros
            ps.setInt(1, pag.getAlunoId());
            ps.setInt(2, pag.getPlanoId());
            ps.setDate(3, Date.valueOf(pag.getDataPagamento()));
            ps.setDate(4, Date.valueOf(pag.getDataVencimento()));
            ps.setString(5, pag.getStatus());

            // Atualiza o plano do aluno na tabela usuarios para manter sincronização
            atualizarPlanoAluno(conn, pag.getAlunoId(), pag.getPlanoId());

            // Executa INSERT e recupera o ID gerado
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pag.setId(rs.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao registrar pagamento: " + e.getMessage());
        }
        return false;
    }

    /**
     * Método verificarUltimoPagamento - Busca o pagamento mais recente de um aluno
     * 
     * @param alunoId ID do aluno
     * @return Objeto Pagamento do último pagamento, ou null se não houver
     * 
     * Processo:
     * 1. Busca todo pagamento do aluno
     * 2. Ordena por data_pagamento DESC (mais recente primeiro)
     * 3. Retorna apenas o primeiro resultado
     */
    public Pagamento verificarUltimoPagamento(int alunoId) {
        // SQL SELECT ordenado por data DESC para pegar o mais recente
        String sql = "SELECT * FROM pagamentos WHERE aluno_id = ? ORDER BY data_pagamento DESC LIMIT 1";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, alunoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao verificar pagamento: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método buscarPorAluno - Retorna todo o histórico de pagamentos de um aluno
     * 
     * @param alunoId ID do aluno
     * @return Lista com todos os pagamentos do aluno, ordenados por data (mais recente primeiro)
     * 
     * Processo:
     * 1. Busca todos os pagamentos do aluno
     * 2. Ordena por data_pagamento DESC
     * 3. Adiciona cada um à lista
     */
    public List<Pagamento> buscarPorAluno(int alunoId) {
        // Lista para armazenar os pagamentos
        List<Pagamento> lista = new ArrayList<>();
        
        // SQL SELECT de todos os pagamentos do aluno, ordenado por data DESC
        String sql = "SELECT * FROM pagamentos WHERE aluno_id = ? ORDER BY data_pagamento DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, alunoId);
            ResultSet rs = ps.executeQuery();
            // Itera sobre cada linha do resultado
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar pagamentos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método atualizarPlanoAluno - Atualiza o plano_id do aluno na tabela usuarios
     * MÉTODO INTERNO: Mantém a sincronização entre pagamentos e plano atual do aluno
     * 
     * @param conn Conexão já aberta com o banco
     * @param alunoId ID do aluno a ser atualizado
     * @param planoId ID do novo plano
     * @throws SQLException Se houver erro na execução
     */
    private void atualizarPlanoAluno(Connection conn, int alunoId, int planoId) throws SQLException {
        // SQL UPDATE para atualizar plano_id do aluno
        String sql = "UPDATE usuarios SET plano_id = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planoId);
            ps.setInt(2, alunoId);
            ps.executeUpdate();
        }
    }

    /**
     * Método mapear - Converte uma linha do banco de dados em um objeto Pagamento
     * MÉTODO INTERNO: Utilitário para conversão de dados
     * 
     * @param rs ResultSet contendo os dados do pagamento
     * @return Objeto Pagamento preenchido com os dados
     * @throws SQLException Se houver erro ao acessar os dados
     */
    private Pagamento mapear(ResultSet rs) throws SQLException {
        return new Pagamento(
            rs.getInt("id"),                                    // ID do pagamento
            rs.getInt("aluno_id"),                              // ID do aluno
            rs.getInt("plano_id"),                              // ID do plano pago
            rs.getDate("data_pagamento").toLocalDate(),         // Data em que foi pago
            rs.getDate("data_vencimento").toLocalDate(),        // Data de vencimento
            rs.getString("status")                              // Status: "pago", "pendente", "vencido"
        );
    }
}
