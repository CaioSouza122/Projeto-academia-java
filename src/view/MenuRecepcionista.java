package view;

import dao.AlunoDAO;
import dao.PagamentoDAO;
import dao.PlanoDAO;
import dao.ProfessorDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import model.Aluno;
import model.Pagamento;
import model.Plano;
import model.Professor;
import model.Recepcionista;

/**
 * Classe MenuRecepcionista - Menu principal para recepcionistas da academia
 * 
 * Responsabilidades:
 * - Criar cadastro de novos alunos
 * - Registrar e gerenciar pagamentos
 * - Atribuir professores aos alunos
 * - Excluir alunos (soft delete)
 * - Listar alunos ativos
 * 
 * Atributos:
 * - recepcionista: Objeto do usuário recepcionista logado
 * - sc: Scanner para capturar entradas
 * - DAOs: Para acesso aos dados (AlunoDAO, ProfessorDAO, PlanoDAO, PagamentoDAO)
 */
public class MenuRecepcionista {

    private Recepcionista recepcionista;
    private Scanner sc;
    private AlunoDAO alunoDAO = new AlunoDAO();
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private PlanoDAO planoDAO = new PlanoDAO();
    private PagamentoDAO pagamentoDAO = new PagamentoDAO();

    /**
     * Construtor MenuRecepcionista
     * @param recepcionista Recepcionista logado
     * @param sc Scanner para entradas
     */
    public MenuRecepcionista(Recepcionista recepcionista, Scanner sc) {
        this.recepcionista = recepcionista;
        this.sc = sc;
    }

    /**
     * Método exibir - Exibe o menu principal do recepcionista
     * 
     * Menu com opções:
     * 1. Criar cadastro de aluno
     * 2. Verificar/registrar pagamento
     * 3. Atribuir professor ao aluno
     * 4. Excluir aluno
     * 5. Listar alunos ativos
     * 0. Sair
     */
    public void exibir() {
        int opcao;
        do {
            System.out.println("\n══════════════════════════════");
            System.out.println("  Recepção — " + recepcionista.getNome());
            System.out.println("  [1] Criar Cadastro de Aluno");
            System.out.println("  [2] Verificar / Registrar Pagamento");
            System.out.println("  [3] Atribuir Professor ao Aluno");
            System.out.println("  [4] Excluir Aluno");
            System.out.println("  [5] Listar Todos os Alunos");
            System.out.println("  [0] Sair");
            System.out.println("══════════════════════════════");
            System.out.print("Opção: ");

            opcao = lerInt();
            switch (opcao) {
                case 1 -> criarAluno();
                case 2 -> gerenciarPagamento();
                case 3 -> atribuirProfessor();
                case 4 -> excluirAluno();
                case 5 -> listarAlunos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Método criarAluno - Cria cadastro de um novo aluno
     * 
     * Processo:
     * 1. Solicita dados pessoais (nome, CPF, email, senha)
     * 2. Mostra planos disponíveis e permite escolher
     * 3. Cria aluno no banco de dados
     * 4. Se plano foi selecionado, registra pagamento automaticamente
     */
    private void criarAluno() {
        System.out.println("\n--- Novo Cadastro de Aluno ---");
        
        // Solicita dados pessoais
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        
        System.out.print("CPF (xxx.xxx.xxx-xx): ");
        String cpf = sc.nextLine().trim();
        
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        // Mostra planos disponíveis
        System.out.println("\nPlanos disponíveis:");
        List<Plano> planos = planoDAO.buscarTodos();
        planos.forEach(System.out::println);
        
        System.out.print("ID do plano (0 para nenhum): ");
        int planoId = lerInt();

        // Cria objeto aluno com dados informados
        Aluno aluno = new Aluno(0, nome, cpf, email, senha, "", 0, planoId, true);
        
        // Tenta criar aluno no banco
        if (alunoDAO.criar(aluno)) {
            System.out.println("✅ Aluno '" + nome + "' cadastrado com ID " + aluno.getId() + "!");

            // Se escolheu plano, registra pagamento
            if (planoId > 0) {
                Plano plano = planoDAO.buscarPorId(planoId);
                if (plano != null) {
                    // Define data de hoje como data de pagamento
                    LocalDate hoje = LocalDate.now();
                    // Calcula vencimento baseado na duração do plano
                    LocalDate vencimento = hoje.plusMonths(plano.getDuracaoMeses());
                    
                    // Cria e registra pagamento
                    Pagamento pag = new Pagamento(0, aluno.getId(), planoId, hoje, vencimento, "pago");
                    pagamentoDAO.registrar(pag);
                    System.out.println("✅ Pagamento registrado. Vencimento: " + vencimento);
                }
            }
        } else {
            System.out.println("❌ Erro ao cadastrar aluno. Verifique se CPF/email já estão em uso.");
        }
    }

    /**
     * Método gerenciarPagamento - Verifica e registra novos pagamentos
     * 
     * Processo:
     * 1. Lista alunos e solicita ID
     * 2. Mostra último pagamento do aluno
     * 3. Permite registrar novo pagamento
     */
    private void gerenciarPagamento() {
        System.out.println("\n--- Pagamentos ---");
        
        // Lista alunos
        listarAlunos();
        
        System.out.print("ID do aluno: ");
        int alunoId = lerInt();

        // Busca dados do aluno
        Aluno aluno = alunoDAO.buscarPorId(alunoId);
        if (aluno == null) { 
            System.out.println("Aluno não encontrado."); 
            return; 
        }

        // Exibe nome do aluno
        System.out.println("\nAluno: " + aluno.getNome());
        
        // Busca e exibe último pagamento
        Pagamento ult = pagamentoDAO.verificarUltimoPagamento(alunoId);
        if (ult != null) {
            System.out.println("Último pagamento: " + ult.getDataPagamento() + " | Vencimento: " + ult.getDataVencimento() + " | Status: " + ult.getStatus().toUpperCase());
        } else {
            System.out.println("Nenhum pagamento registrado.");
        }

        // Pergunta se quer registrar novo pagamento
        System.out.print("\nRegistrar novo pagamento? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) return;

        // Mostra planos disponíveis
        System.out.println("\nPlanos disponíveis:");
        planoDAO.buscarTodos().forEach(System.out::println);
        
        System.out.print("ID do plano: ");
        int planoId = lerInt();

        // Busca plano selecionado
        Plano plano = planoDAO.buscarPorId(planoId);
        if (plano == null) { 
            System.out.println("Plano inválido."); 
            return; 
        }

        // Calcula data de vencimento
        LocalDate hoje = LocalDate.now();
        LocalDate vencimento = hoje.plusMonths(plano.getDuracaoMeses());
        
        // Cria e registra pagamento
        Pagamento pag = new Pagamento(0, alunoId, planoId, hoje, vencimento, "pago");
        if (pagamentoDAO.registrar(pag)) {
            System.out.println("✅ Pagamento registrado! Vencimento: " + vencimento);
        }
    }

    /**
     * Método atribuirProfessor - Atribui um professor a um aluno
     * 
     * Processo:
     * 1. Lista alunos e solicita ID
     * 2. Lista professores disponíveis
     * 3. Atribui professor ao aluno
     */
    private void atribuirProfessor() {
        System.out.println("\n--- Atribuir Professor ---");
        
        // Lista alunos
        listarAlunos();
        
        System.out.print("ID do aluno: ");
        int alunoId = lerInt();

        // Mostra professores disponíveis
        System.out.println("\nProfessores disponíveis:");
        List<Professor> profs = professorDAO.buscarTodos();
        
        if (profs.isEmpty()) {
            System.out.println("Nenhum professor cadastrado.");
            return;
        }
        
        profs.forEach(System.out::println);
        
        System.out.print("ID do professor: ");
        int profId = lerInt();

        // Tenta atribuir professor
        if (alunoDAO.atribuirProfessor(alunoId, profId)) {
            System.out.println("✅ Professor atribuído com sucesso!");
        } else {
            System.out.println("❌ Erro ao atribuir professor.");
        }
    }

    /**
     * Método excluirAluno - Exclui (desativa) um aluno
     * 
     * Nota: Realiza soft delete (marca como inativo, não deleta realmente)
     * 
     * Processo:
     * 1. Lista alunos e solicita ID
     * 2. Solicita confirmação
     * 3. Marca aluno como inativo
     */
    private void excluirAluno() {
        System.out.println("\n--- Excluir Aluno ---");
        
        // Lista alunos
        listarAlunos();
        
        System.out.print("ID do aluno a excluir: ");
        int alunoId = lerInt();

        // Solicita confirmação antes de deletar
        System.out.print("Tem certeza? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) return;

        // Tenta excluir (soft delete)
        if (alunoDAO.excluir(alunoId)) {
            System.out.println("✅ Aluno removido com sucesso.");
        } else {
            System.out.println("❌ Erro ao excluir aluno.");
        }
    }

    /**
     * Método listarAlunos - Lista todos os alunos ativos cadastrados
     */
    private void listarAlunos() {
        System.out.println("\n--- Alunos Ativos ---");
        List<Aluno> alunos = alunoDAO.buscarTodos();
        
        if (alunos.isEmpty()) 
            System.out.println("Nenhum aluno cadastrado.");
        else 
            alunos.forEach(System.out::println);
    }

    /**
     * Método lerInt - Lê um inteiro do usuário de forma segura
     * 
     * @return Inteiro lido, ou -1 se houver erro de formato
     */
    private int lerInt() {
        try { 
            return Integer.parseInt(sc.nextLine().trim()); 
        }
        catch (NumberFormatException e) { 
            return -1; 
        }
    }
}
