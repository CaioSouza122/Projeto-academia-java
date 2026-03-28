package dao;

import connection.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Professor;

/**
 * Classe ProfessorDAO responsável por gerenciar todas as operações de acesso a dados
 * relacionadas aos professores/personal trainers
 * 
 * Métodos:
 * - buscarTodos: Retorna lista de todos os professores ativos
 * - buscarPorId: Busca um professor específico pelo ID
 * - criar: Insere um novo professor no banco
 * - mapear: Converte ResultSet em objeto Professor (interno)
 */
public class ProfessorDAO {

    /**
     * Método buscarTodos - Retorna lista de todos os professores ativos
     * 
     * @return Lista de todos os professores, ordenados por nome
     * 
     * Processo:
     * 1. Executa SQL SELECT filtrando por tipo 'professor' e ativo = TRUE
     * 2. Ordena por nome
     * 3. Adiciona cada professor à lista
     */
    public List<Professor> buscarTodos() {
        // Lista para armazenar os professores
        List<Professor> lista = new ArrayList<>();
        
        // SQL SELECT todos os professores ativos, ordenado por nome
        String sql = "SELECT * FROM usuarios WHERE tipo = 'professor' AND ativo = TRUE ORDER BY nome";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Itera sobre cada linha do resultado
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao listar professores: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método buscarPorId - Busca um professor específico pelo seu ID
     * 
     * @param id ID do professor a ser buscado
     * @return Objeto Professor se encontrado, null caso contrário
     * 
     * Processo:
     * 1. Constrói SQL SELECT filtrando por ID e tipo 'professor'
     * 2. Executa a query
     * 3. Se encontrar resultado, mapeia para objeto Professor
     */
    public Professor buscarPorId(int id) {
        // SQL SELECT filtrando professor específico e garantindo que é tipo 'professor'
        String sql = "SELECT * FROM usuarios WHERE id = ? AND tipo = 'professor'";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define o ID na query
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            // Se encontrou resultado, mapeia e retorna
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar professor: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método criar - Insere um novo professor no banco de dados
     * 
     * @param prof Objeto Professor com os dados a serem inseridos
     * @return true se o professor foi criado com sucesso, false caso contrário
     * 
     * Processo:
     * 1. Prepara SQL INSERT com os dados do professor
     * 2. Define tipo como 'professor' automaticamente
     * 3. Executa a query e recupera o ID gerado
     * 4. Atualiza o objeto professor com o ID retornado
     */
    public boolean criar(Professor prof) {
        // SQL INSERT para registrar novo professor (tipo sempre 'professor')
        String sql = "INSERT INTO usuarios (nome, cpf, email, senha, tipo) VALUES (?, ?, ?, ?, 'professor') RETURNING id";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os valores dos parâmetros
            ps.setString(1, prof.getNome());
            ps.setString(2, prof.getCpf());
            ps.setString(3, prof.getEmail());
            ps.setString(4, prof.getSenha());

            // Executa INSERT e recupera o ID gerado
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Obtém o ID gerado e atribui ao objeto professor
                prof.setId(rs.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao criar professor: " + e.getMessage());
        }
        return false;
    }

    /**
     * Método mapear - Converte uma linha do banco de dados em um objeto Professor
     * MÉTODO INTERNO: Utilitário para conversão de dados
     * 
     * @param rs ResultSet contendo os dados do professor
     * @return Objeto Professor preenchido com os dados
     * @throws SQLException Se houver erro ao acessar os dados
     */
    private Professor mapear(ResultSet rs) throws SQLException {
        return new Professor(
            rs.getInt("id"),            // ID único do professor
            rs.getString("nome"),       // Nome completo
            rs.getString("cpf"),        // CPF (cadastro de pessoa física)
            rs.getString("email"),      // Email para contato
            rs.getString("senha")       // Senha (criptografada)
        );
    }
}
