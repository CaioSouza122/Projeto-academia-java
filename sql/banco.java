// Classe DAO com todas as operações principais do sistema
public class SistemaDAO {

    // LOGIN: valida email e senha criptografada
    public static boolean login(String email, String senha) {
        String sql = "SELECT * FROM alunos WHERE email = ? AND senha = crypt(?, senha)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // CADASTRO DE ALUNO: insere aluno com senha criptografada
    public static void cadastrarAluno(String nome, String cpf, String email, String senha, String telefone) {
        String sql = "INSERT INTO alunos (nome, cpf, email, senha, telefone) VALUES (?, ?, ?, crypt(?, gen_salt('bf')), ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, email);
            stmt.setString(4, senha);
            stmt.setString(5, telefone);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PAGAMENTO: registra pagamento do aluno
    public static void registrarPagamento(int alunoId, double valor) {
        String sql = "INSERT INTO pagamentos (aluno_id, status, valor, data_pagamento) VALUES (?, 'pago', ?, CURRENT_DATE)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            stmt.setDouble(2, valor);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // STATUS DO ALUNO: consulta último pagamento pelo CPF
    public static void verStatus(String cpf) {
        String sql = """
            SELECT a.nome, p.status 
            FROM alunos a 
            LEFT JOIN pagamentos p ON p.aluno_id = a.id 
            WHERE a.cpf = ?
            ORDER BY p.data_pagamento DESC
            LIMIT 1
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Status: " + rs.getString("status"));
            } else {
                System.out.println("Aluno não encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PROFESSOR: lista alunos ativos vinculados
    public static void alunosDoProfessor(int professorId) {
        String sql = "SELECT nome FROM alunos WHERE professor_id = ? AND ativo = true";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("nome"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TREINO: cadastra treino com duração mínima de 3 meses
    public static void cadastrarTreino(int alunoId, int professorId, String descricao) {
        String sql = """
            INSERT INTO treinos (aluno_id, professor_id, descricao, data_inicio, data_fim)
            VALUES (?, ?, ?, CURRENT_DATE, CURRENT_DATE + INTERVAL '3 months')
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            stmt.setInt(2, professorId);
            stmt.setString(3, descricao);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TREINO DO ALUNO: consulta treino e professor responsável
    public static void verTreino(int alunoId) {
        String sql = """
            SELECT t.descricao, p.nome 
            FROM treinos t 
            JOIN professores p ON t.professor_id = p.id 
            WHERE t.aluno_id = ?
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Treino: " + rs.getString("descricao"));
                System.out.println("Professor: " + rs.getString("nome"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}