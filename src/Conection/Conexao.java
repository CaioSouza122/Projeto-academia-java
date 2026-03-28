package Conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Conexao responsável por gerenciar a conexão com o banco de dados PostgreSQL
 * 
 * IMPORTANTE: Altere as credenciais (URL, USUARIO, SENHA) de acordo com sua configuração local
 */
public class Conexao {

    // URL de conexão: localhost na porta 5432 com o banco de dados 'NomeBanco'
    private static final String URL = "jdbc:postgresql://localhost:5432/NomeBanco";
    
    // Usuário padrão do PostgreSQL
    private static final String USUARIO = "postgres";
    
    // Senha configurada para o usuário postgres
    private static final String SENHA = "root";

    /**
     * Método conectar - Estabelece uma conexão com o banco de dados PostgreSQL
     * 
     * @return Connection se conseguir conectar, null se houver erro
     * 
     * Processo:
     * 1. Tenta estabelecer conexão usando DriverManager.getConnection()
     * 2. Se sucesso, exibe mensagem e retorna a conexão
     * 3. Se erro, exibe mensagem de exceção e retorna null
     */
    public static Connection conectar() {
        try {
            // Tenta estabelecer conexão com o banco usando URL, usuário e senha
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado ao banco de dados com sucesso!");
            return conn;
        } catch (SQLException e) {
            // Captura erros de SQL (conexão recusada, credenciais inválidas, etc)
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
}
