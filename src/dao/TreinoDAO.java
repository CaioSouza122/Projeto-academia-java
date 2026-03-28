package dao;

import connection.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Exercicio;
import model.Treino;

/**
 * Classe TreinoDAO responsável por gerenciar todas as operações de acesso a dados
 * relacionadas aos treinos e exercícios dos alunos
 * 
 * Métodos:
 * - criar: Insere um novo treino no banco
 * - adicionarExercicio: Adiciona um exercício a um treino
 * - buscarPorAluno: Retorna todos os treinos de um aluno com seus exercícios
 * - buscarPorProfessor: Retorna todos os treinos criados por um professor
 * - buscarExercicios: Busca os exercícios de um treino específico (interno)
 * - mapear: Converte ResultSet em objeto Treino (interno)
 */
public class TreinoDAO {

    /**
     * Método criar - Insere um novo treino no banco de dados
     * 
     * @param treino Objeto Treino com os dados a serem inseridos
     * @return true se o treino foi criado com sucesso, false caso contrário
     * 
     * Processo:
     * 1. Prepara SQL INSERT com os dados do treino
     * 2. Executa a query e recupera o ID gerado
     * 3. Atualiza o objeto treino com o ID retornado
     */
    public boolean criar(Treino treino) {
        // SQL INSERT para registrar novo treino
        String sql = "INSERT INTO treinos (nome, aluno_id, professor_id, descricao) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os valores dos parâmetros
            ps.setString(1, treino.getNome());
            ps.setInt(2, treino.getAlunoId());
            ps.setInt(3, treino.getProfessorId());
            ps.setString(4, treino.getDescricao());

            // Executa INSERT e recupera o ID gerado
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                treino.setId(rs.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao criar treino: " + e.getMessage());
        }
        return false;
    }

    /**
     * Método adicionarExercicio - Adiciona um exercício a um treino existente
     * 
     * @param ex Objeto Exercicio com os dados a serem inseridos
     * @return true se o exercício foi adicionado com sucesso, false caso contrário
     * 
     * Processo:
     * 1. Prepara SQL INSERT para a tabela exercicios
     * 2. Define todos os parâmetros (séries, repetições, descanso)
     * 3. Executa a query e recupera o ID gerado
     * 4. Atualiza o objeto exercício com o ID retornado
     */
    public boolean adicionarExercicio(Exercicio ex) {
        // SQL INSERT para adicionar novo exercício a um treino
        String sql = "INSERT INTO exercicios (treino_id, nome, series, repeticoes, descanso_seg) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os valores dos parâmetros
            ps.setInt(1, ex.getTreinoId());
            ps.setString(2, ex.getNome());
            ps.setInt(3, ex.getSeries());
            ps.setInt(4, ex.getRepeticoes());
            ps.setInt(5, ex.getDescansoSeg());

            // Executa INSERT e recupera o ID gerado
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ex.setId(rs.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao adicionar exercício: " + e.getMessage());
        }
        return false;
    }

    /**
     * Método buscarPorAluno - Retorna todos os treinos de um aluno com seus exercícios
     * 
     * @param alunoId ID do aluno
     * @return Lista de todos os treinos do aluno com os exercícios preenchidos
     * 
     * Processo:
     * 1. Busca todos os treinos do aluno, ordenados por data (mais recente primeiro)
     * 2. Para cada treino, busca seus exercícios relacionados
     * 3. Adiciona o treino com exercícios à lista
     */
    public List<Treino> buscarPorAluno(int alunoId) {
        // Lista para armazenar os treinos
        List<Treino> lista = new ArrayList<>();
        
        // SQL SELECT todos os treinos do aluno, ordenado por data DESC
        String sql = "SELECT * FROM treinos WHERE aluno_id = ? ORDER BY data_criacao DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, alunoId);
            ResultSet rs = ps.executeQuery();
            // Itera sobre cada treino encontrado
            while (rs.next()) {
                Treino t = mapear(rs);
                // Para cada treino, busca seus exercícios e os adiciona à lista do treino
                t.setExercicios(buscarExercicios(t.getId()));
                lista.add(t);
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar treinos do aluno: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método buscarPorProfessor - Retorna todos os treinos criados por um professor
     * 
     * @param professorId ID do professor
     * @return Lista de todos os treinos criados pelo professor com exercícios preenchidos
     * 
     * Processo:
     * 1. Busca todos os treinos do professor, ordenados por data (mais recente primeiro)
     * 2. Para cada treino, busca seus exercícios relacionados
     * 3. Adiciona o treino com exercícios à lista
     */
    public List<Treino> buscarPorProfessor(int professorId) {
        // Lista para armazenar os treinos
        List<Treino> lista = new ArrayList<>();
        
        // SQL SELECT todos os treinos do professor, ordenado por data DESC
        String sql = "SELECT * FROM treinos WHERE professor_id = ? ORDER BY data_criacao DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, professorId);
            ResultSet rs = ps.executeQuery();
            // Itera sobre cada treino encontrado
            while (rs.next()) {
                Treino t = mapear(rs);
                // Para cada treino, busca seus exercícios e os adiciona à lista do treino
                t.setExercicios(buscarExercicios(t.getId()));
                lista.add(t);
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar treinos do professor: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método buscarExercicios - Busca todos os exercícios de um treino específico
     * MÉTODO INTERNO: Utilitário para buscar exercícios de um treino
     * 
     * @param treinoId ID do treino
     * @return Lista com todos os exercícios do treino
     * 
     * Processo:
     * 1. Executa SQL SELECT filtrando por treino_id
     * 2. Para cada linha, cria um objeto Exercicio
     * 3. Adiciona à lista e retorna
     */
    private List<Exercicio> buscarExercicios(int treinoId) {
        // Lista para armazenar os exercícios
        List<Exercicio> lista = new ArrayList<>();
        
        // SQL SELECT todos os exercícios de um treino específico
        String sql = "SELECT * FROM exercicios WHERE treino_id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, treinoId);
            ResultSet rs = ps.executeQuery();
            // Itera sobre cada exercício
            while (rs.next()) {
                lista.add(new Exercicio(
                    rs.getInt("id"),
                    rs.getInt("treino_id"),
                    rs.getString("nome"),
                    rs.getInt("series"),
                    rs.getInt("repeticoes"),
                    rs.getInt("descanso_seg")
                ));
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro ao buscar exercícios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método mapear - Converte uma linha do banco de dados em um objeto Treino
     * MÉTODO INTERNO: Utilitário para conversão de dados
     * 
     * @param rs ResultSet contendo os dados do treino (SEM exercícios)
     * @return Objeto Treino preenchido com os dados
     * @throws SQLException Se houver erro ao acessar os dados
     */
    private Treino mapear(ResultSet rs) throws SQLException {
        return new Treino(
            rs.getInt("id"),                // ID único do treino
            rs.getString("nome"),           // Nome/identificação do treino
            rs.getInt("aluno_id"),          // ID do aluno a quem pertence
            rs.getInt("professor_id"),      // ID do professor que criou
            rs.getString("descricao")       // Descrição do treino
        );
    }
}
