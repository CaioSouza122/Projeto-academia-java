package dao;

import Conection.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Plano;

/**
 * Classe PlanoDAO responsável por gerenciar todas as operações de acesso a dados
 * relacionadas aos planos de academia (listagem)
 * 
 * NOTA: Esta classe só faz leitura de dados (não há inserção/atualização de planos neste sistema)
 * 
 * Métodos:
 * - buscarTodos: Retorna lista de todos os planos disponíveis
 * - buscarPorId: Busca um plano específico pelo ID
 * - mapear: Converte ResultSet em objeto Plano (interno)
 */
public class PlanoDAO {

    /**
     * Método buscarTodos - Retorna lista com todos os planos disponíveis
     * 
     * @return Lista de todos os planos ordenados por valor (crescente)
     * 
     * Processo:
     * 1. Executa SQL SELECT em todos os planos
     * 2. Ordena por valor para mostrar do mais barato ao mais caro
     * 3. Adiciona cada plano à lista e retorna
     */
    public List<Plano> buscarTodos() {
        // Lista para armazenar os planos
        List<Plano> lista = new ArrayList<>();
        
        // SQL SELECT de todos os planos, ordenado por valor
        String sql = "SELECT * FROM planos ORDER BY valor";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Itera sobre cada linha do resultado
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao listar planos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método buscarPorId - Busca um plano específico pelo seu ID
     * 
     * @param id ID do plano a ser buscado
     * @return Objeto Plano se encontrado, null caso contrário
     * 
     * Processo:
     * 1. Constrói SQL SELECT filtrando por ID
     * 2. Executa a query
     * 3. Se encontrar resultado, mapeia para objeto Plano
     */
    public Plano buscarPorId(int id) {
        // SQL SELECT filtrando plano específico por ID
        String sql = "SELECT * FROM planos WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define o ID na query
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            // Se encontrou resultado, mapeia e retorna
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar plano: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método mapear - Converte uma linha do banco de dados em um objeto Plano
     * MÉTODO INTERNO: Utilitário para conversão de dados
     * 
     * @param rs ResultSet contendo os dados do plano
     * @return Objeto Plano preenchido com os dados
     * @throws SQLException Se houver erro ao acessar os dados
     */
    private Plano mapear(ResultSet rs) throws SQLException {
        return new Plano(
            rs.getInt("id"),                    // Identificador único do plano
            rs.getString("nome"),               // Nome do plano (ex: "Plano Básico")
            rs.getDouble("valor"),              // Valor mensal do plano
            rs.getString("descricao"),          // Descrição do que inclui o plano
            rs.getInt("duracao_meses")          // Duração do plano em meses
        );
    }
}
