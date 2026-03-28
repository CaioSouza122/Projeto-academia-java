package view;

import connection.Conexao;
import java.sql.*;
import java.util.Scanner;
import model.Aluno;
import model.Professor;
import model.Recepcionista;

/**
 * Classe MenuLogin - Tela inicial de login do sistema
 * 
 * Responsabilidades:
 * - Exibir formulário de login (email e senha)
 * - Validar credenciais no banco de dados
 * - Direcionar usuário para o menu apropriado (Aluno, Professor ou Recepcionista)
 * - Permitir novo login após sair de um menu
 * 
 * Atributos:
 * - sc: Scanner para capturar entradas do usuário
 */
public class MenuLogin {

    private Scanner sc;

    /**
     * Construtor MenuLogin
     * @param sc Scanner para capturar entradas do usuário
     */
    public MenuLogin(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Método exibir - Exibe a tela de login e controla o fluxo de atenticação
     * 
     * Fluxo:
     * 1. Exibe o título da aplicação
     * 2. Em loop contínuo:
     *    a. Solicita email e senha
     *    b. Chama autenticar() para validar as credenciais
     *    c. Se inválido, exibe erro e volta ao início do loop
     *    d. Se válido, direciona para o menu apropriado baseado no tipo de usuário
     *    e. Após sair do menu, pergunta se deseja fazer novo login
     * 3. Se responder "não", encerra a aplicação
     */
    public void exibir() {
        // Exibe cabeçalho da aplicação
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║     GymFlow — Sistema Academia   ║");
        System.out.println("╚══════════════════════════════════╝");

        // Loop contínuo de tentativas de login
        while (true) {
            // Solicita email ao usuário
            System.out.print("\nEmail: ");
            String email = sc.nextLine().trim();
            
            // Solicita senha ao usuário
            System.out.print("Senha: ");
            String senha = sc.nextLine().trim();

            // Tenta autenticar o usuário
            Object usuario = autenticar(email, senha);

            // Se autenticação falhou (retornou null), exibe mensagem de erro
            if (usuario == null) {
                System.out.println("❌ Email ou senha incorretos. Tente novamente.");
                continue;
            }

            // Se é um aluno, direciona para o menu de aluno
            if (usuario instanceof Aluno) {
                new MenuAluno((Aluno) usuario, sc).exibir();
            } 
            // Se é um professor, direciona para o menu de professor
            else if (usuario instanceof Professor) {
                new MenuProfessor((Professor) usuario, sc).exibir();
            } 
            // Se é um recepcionista, direciona para o menu de recepcionista
            else if (usuario instanceof Recepcionista) {
                new MenuRecepcionista((Recepcionista) usuario, sc).exibir();
            }

            // Após sair do menu, pergunta se quer fazer novo login
            System.out.print("\nDeseja fazer novo login? (s/n): ");
            String resp = sc.nextLine().trim();
            // Se responde "não", sai do loop e encerra
            if (!resp.equalsIgnoreCase("s")) break;
        }

        // Mensagem de encerramento
        System.out.println("\nAté logo! Sistema encerrado.");
    }

    /**
     * Método autenticar - Valida as credenciais de login no banco de dados
     * 
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return Objeto do usuário (Aluno, Professor ou Recepcionista) se autenticado
     *         null se as credenciais forem inválidas
     * 
     * Processo:
     * 1. Executa SQL SELECT buscando usuário com email e senha informados
     * 2. Se encontrar e estiver ativo, mapeia para o tipo apropriado
     * 3. Retorna o objeto or null se não encontrar
     */
    private Object autenticar(String email, String senha) {
        // SQL SELECT para buscar usuário ativo com as credenciais informadas
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ? AND ativo = TRUE";
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Define os parâmetros da query
            ps.setString(1, email);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();

            // Se encontrou o usuário
            if (rs.next()) {
                // Recupera informações básicas
                String tipo = rs.getString("tipo");
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");

                // Cria e retorna o objeto apropriado baseado no tipo
                switch (tipo) {
                    case "aluno":
                        // Para aluno: recupera professor_id e plano_id
                        return new Aluno(id, nome, cpf, email, senha, "",
                            rs.getObject("professor_id") != null ? rs.getInt("professor_id") : 0,
                            rs.getObject("plano_id") != null ? rs.getInt("plano_id") : 0,
                            rs.getBoolean("ativo"));
                    case "professor":
                        // Para professor: dados básicos
                        return new Professor(id, nome, cpf, email, senha);
                    case "recepcionista":
                        // Para recepcionista: dados básicos
                        return new Recepcionista(id, nome, cpf, email, senha);
                }
            }
        } catch (SQLException e) {
            // Captura e exibe erros de SQL
            System.out.println("Erro no login: " + e.getMessage());
        }
        
        // Retorna null se não autenticou
        return null;
    }
}
