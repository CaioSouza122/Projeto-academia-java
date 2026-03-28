package dao;

import Conection.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Aluno;

/**
 * Classe AlunoDAO responsável por todas as operações de acesso a dados (Data Access Object)
 * relacionadas aos alunos no banco de dados.
 * 
 * Métodos:
 * - criar: Insere um novo aluno no banco
 * - buscarPorId: Busca um aluno específico pelo ID
 * - buscarTodos: Retorna lista de todos os alunos ativos
 * - buscarPorProfessor: Retorna alunos de um professor específico
 * - atribuirProfessor: Atribui um professor a um aluno
 * - excluir: Desativa um aluno (soft delete)
 */
public class AlunoDAO {

    /**
     * Método criar - Insere um novo aluno no banco de dados
     * 
     * @param aluno Objeto Aluno com os dados a serem inseridos
     * @return true se o aluno foi criado com sucesso, false caso contrário
     * 
     * Processo:
     * 1. Prepara SQL INSERT com placeholders (?) para evitar SQL Injection
     * 2. Define os valores dos parâmetros (nome, cpf, email, senha, professor_id, plano_id)
     * 3. Executa a query e recupera o ID gerado automaticamente
     * 4. Atualiza o objeto aluno com o ID retornado do banco
     */
    public boolean criar(Aluno aluno) {
        // SQL com RETURNING id para recuperar o ID gerado automaticamente pelo banco
        String sql = "INSERT INTO usuarios (nome, cpf, email, senha, tipo, professor_id, plano_id) " +
                     "VALUES (?, ?, ?, ?, 'aluno', ?, ?) RETURNING id";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os valores dos parâmetros usando setString e setInt para evitar SQL injection
            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getCpf());
            ps.setString(3, aluno.getEmail());
            ps.setString(4, aluno.getSenha());
            
            // Verifica se professor_id é 0 (não informado) e define NULL no banco se for
            if (aluno.getProfessorId() == 0) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, aluno.getProfessorId());
            
            // Verifica se plano_id é 0 (não informado) e define NULL no banco se for
            if (aluno.getPlanoId() == 0) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, aluno.getPlanoId());

            // Executa a query e recupera o resultado
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Obtém o ID gerado automaticamente e o atribui ao objeto aluno
                aluno.setId(rs.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao criar aluno: " + e.getMessage());
        }
        return false;
    }


    /**
     * Método buscarPorId - Busca um aluno específico pelo seu ID
     * 
     * @param id ID do aluno a ser buscado
     * @return Objeto Aluno se encontrado, null caso contrário
     * 
     * Processo:
     * 1. Constrói SQL SELECT filtrando por ID e tipo 'aluno'
     * 2. Executa a query e recupera os dados
     * 3. Mapeia o ResultSet para um objeto Aluno
     */
    public Aluno buscarPorId(int id) {
        // SQL SELECT filtrando o aluno específico e garantindo que é do tipo 'aluno'
        String sql = "SELECT * FROM usuarios WHERE id = ? AND tipo = 'aluno'";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define o valor do ID na query
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            // Se encontrou resultado, mapeia para objeto Aluno e retorna
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        }
        return null;
    }


    /**
     * Método buscarTodos - Retorna uma lista de todos os alunos ativos no banco
     * 
     * @return Lista de objetos Aluno ordenados por nome
     * 
     * Processo:
     * 1. Constrói SQL SELECT filtrando alunos ativos
     * 2. Ordena os resultados por nome
     * 3. Itera os resultados e adiciona cada um à lista
     */
    public List<Aluno> buscarTodos() {
        // Lista para armazenar os alunos encontrados
        List<Aluno> lista = new ArrayList<>();
        
        // SQL SELECT todos os alunos ativos, ordenado por nome
        // ativo = TRUE garante que não retorna alunos deletados (soft delete)
        String sql = "SELECT * FROM usuarios WHERE tipo = 'aluno' AND ativo = TRUE ORDER BY nome";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Itera sobre cada linha do resultado
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        }
        return lista;
    }


    /**
     * Método buscarPorProfessor - Busca todos os alunos de um professor específico
     * 
     * @param professorId ID do professor cujos alunos serão recuperados
     * @return Lista de objetos Aluno do professor solicitado
     * 
     * Processo:
     * 1. Constrói SQL SELECT filtrando por professor_id
     * 2. Retorna apenas alunos ativos
     * 3. Ordena por nome para melhor visualização
     */
    public List<Aluno> buscarPorProfessor(int professorId) {
        // Lista para armazenar os alunos do professor
        List<Aluno> lista = new ArrayList<>();
        
        // SQL SELECT alunos de um professor específico, apenas alunos ativos, ordenado por nome
        String sql = "SELECT * FROM usuarios WHERE tipo = 'aluno' AND professor_id = ? AND ativo = TRUE ORDER BY nome";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define o ID do professor na query
            ps.setInt(1, professorId);
            ResultSet rs = ps.executeQuery();
            
            // Itera sobre cada linha do resultado
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar alunos do professor: " + e.getMessage());
        }
        return lista;
    }


    /**
     * Método atribuirProfessor - Atribui um professor a um aluno
     * 
     * @param alunoId ID do aluno que receberá o professor
     * @param professorId ID do professor a ser atribuído
     * @return true se a atribuição foi bem-sucedida, false caso contrário
     * 
     * Processo:
     * 1. Constrói SQL UPDATE para modificar o professor_id do aluno
     * 2. Executa a query e verifica se os dados foram atualizados
     */
    public boolean atribuirProfessor(int alunoId, int professorId) {
        // SQL UPDATE para atualizar o professor_id do aluno
        String sql = "UPDATE usuarios SET professor_id = ? WHERE id = ? AND tipo = 'aluno'";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os valores dos parâmetros: novo professor_id e ID do aluno
            ps.setInt(1, professorId);
            ps.setInt(2, alunoId);
            
            // executeUpdate retorna a quantidade de linhas afetadas (> 0 se atualizou)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao atribuir professor: " + e.getMessage());
        }
        return false;
    }


    /**
     * Método excluir - Desativa um aluno (soft delete)
     * 
     * @param id ID do aluno a ser desativado
     * @return true se o aluno foi desativado com sucesso, false caso contrário
     * 
     * IMPORTANTE: Este método não deleta realmente o registro, apenas marca como inativo (ativo = FALSE)
     * Isso permite manter histórico de dados e relacionamentos intactos no banco de dados
     * 
     * Processo:
     * 1. Constrói SQL UPDATE para marcar o aluno como inativo
     * 2. Executa a query e verifica se foi atualizado
     */
    public boolean excluir(int id) {
        // SQL UPDATE que marca o aluno como inativo (soft delete)
        // Nota: ativo = FALSE não deleta dados, apenas marca como deletado
        String sql = "UPDATE usuarios SET ativo = FALSE WHERE id = ? AND tipo = 'aluno'";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define o ID do aluno a ser desativado
            ps.setInt(1, id);
            
            // executeUpdate retorna a quantidade de linhas afetadas (> 0 se atualizou)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
        }
        return false;
    }


    /**
     * Método mapear - Converte uma linha do banco de dados (ResultSet) em um objeto Aluno
     * 
     * @param rs ResultSet contendo os dados do aluno
     * @return Objeto Aluno preenchido com os dados do banco
     * @throws SQLException Se houver erro ao acessar os dados do ResultSet
     * 
     * Processo:
     * 1. Recupera cada coluna do ResultSet
     * 2. Trata valores NULL (especialmente professor_id e plano_id)
     * 3. Cria e retorna um novo objeto Aluno com os dados
     */
    private Aluno mapear(ResultSet rs) throws SQLException {
        // Cria e retorna um novo Aluno com os dados do ResultSet
        return new Aluno(
            rs.getInt("id"),                                                    // ID do aluno
            rs.getString("nome"),                                               // Nome completo
            rs.getString("cpf"),                                                // CPF (cadastro de pessoa física)
            rs.getString("email"),                                              // Email para contato
            rs.getString("senha"),                                              // Senha (criptografada)
            "",                                                                 // telefone vazio (não está no banco)
            // professor_id: verifica se é NULL no banco, se for retorna 0, senão retorna o valor
            rs.getObject("professor_id") != null ? rs.getInt("professor_id") : 0,
            // plano_id: verifica se é NULL no banco, se for retorna 0, senão retorna o valor
            rs.getObject("plano_id") != null ? rs.getInt("plano_id") : 0,
            rs.getBoolean("ativo")                                              // Flag indicando se aluno está ativo
        );
    }
}

