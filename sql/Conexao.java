package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Conexao - Gerencia a conexão com o banco de dados PostgreSQL
 * 
 * Responsabilidades:
 * - Armazenar credenciais de acesso ao banco
 * - Prover um método estático para abrir novas conexões
 * - Tratar erros de conexão via SQLException
 * 
 * Configuração:
 * - URL: Protocolo jdbc:postgresql seguido do endereço e nome do banco
 * - USUARIO: Usuário do banco (default: postgres)
 * - SENHA: Senha do usuário do banco
 */
public class Conexao {

    // Configurações do Banco de Dados
    // TODO: Altere localhost pelo IP do seu servidor se o banco não estiver nesta máquina
    // TODO: Altere 'NomeBanco' pelo nome do banco que você criou no PostgreSQL
    private static final String URL     = "jdbc:postgresql://localhost:5432/NomeBanco";
    private static final String USUARIO = "postgres";
    private static final String SENHA   = "root";

    /**
     * Método conectar - Abre uma nova conexão com o PostgreSQL
     * 
     * @return Objeto Connection se a conexão for bem-sucedida, null caso contrário
     */
    public static Connection conectar() {
        try {
            // Tenta estabelecer a conexão usando o driver JDBC
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            
            // Log de sucesso (útil para debug)
            // System.out.println("Conectado ao banco de dados com sucesso!");
            
            return conn;
        } catch (SQLException e) {
            // Captura e exibe erros de conexão (ex: senha errada, banco offline)
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
}
