package view;

import dao.AlunoDAO;
import dao.PagamentoDAO;
import dao.PlanoDAO;
import dao.TreinoDAO;
import java.util.List;
import java.util.Scanner;
import model.Aluno;
import model.Exercicio;
import model.Pagamento;
import model.Plano;
import model.Professor;
import model.Treino;

/**
 * Classe MenuProfessor - Menu principal para professores/personal trainers
 * 
 * Responsabilidades:
 * - Listar alunos designados ao professor
 * - Criar e montar treinos para os alunos
 * - Verificar planos e pagamentos dos alunos
 * - Gerenciar exercícios dos treinos
 * 
 * Atributos:
 * - professor: Objeto do usuário professor logado
 * - sc: Scanner para capturar entradas
 * - DAOs: Para acesso aos dados (AlunoDAO, TreinoDAO, PlanoDAO, PagamentoDAO)
 */
public class MenuProfessor {

    private Professor professor;
    private Scanner sc;
    private AlunoDAO alunoDAO = new AlunoDAO();
    private TreinoDAO treinoDAO = new TreinoDAO();
    private PlanoDAO planoDAO = new PlanoDAO();
    private PagamentoDAO pagamentoDAO = new PagamentoDAO();

    /**
     * Construtor MenuProfessor
     * @param professor Professor logado
     * @param sc Scanner para entradas
     */
    public MenuProfessor(Professor professor, Scanner sc) {
        this.professor = professor;
        this.sc = sc;
    }

    /**
     * Método exibir - Exibe o menu principal do professor
     * 
     * Menu com opções:
     * 1. Listar meus alunos
     * 2. Montar treino para aluno
     * 3. Verificar plano de aluno
     * 0. Sair
     */
    public void exibir() {
        int opcao;
        do {
            System.out.println("\n══════════════════════════════");
            System.out.println("  Prof. " + professor.getNome());
            System.out.println("  [1] Meus Alunos");
            System.out.println("  [2] Montar Treino para Aluno");
            System.out.println("  [3] Verificar Plano de Aluno");
            System.out.println("  [0] Sair");
            System.out.println("══════════════════════════════");
            System.out.print("Opção: ");

            opcao = lerInt();
            switch (opcao) {
                case 1 -> listarAlunos();
                case 2 -> montarTreino();
                case 3 -> verificarPlanoAluno();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Método listarAlunos - Exibe lista de alunos designados ao professor
     */
    private void listarAlunos() {
        System.out.println("\n--- Meus Alunos ---");
        
        // Busca todos os alunos do professor
        List<Aluno> alunos = alunoDAO.buscarPorProfessor(professor.getId());
        
        // Se não houver alunos, exibe mensagem
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno atribuído a você.");
            return;
        }
        
        // Exibe cada aluno
        alunos.forEach(System.out::println);
    }

    /**
     * Método montarTreino - Cria um novo treino para um aluno com exercícios
     * 
     * Processo:
     * 1. Lista alunos do professor
     * 2. Solicita dados do treino (nome e descrição)
     * 3. Cria o treino no banco
     * 4. Permite adicionar exercícios ao treino
     */
    private void montarTreino() {
        // Busca alunos do professor
        List<Aluno> alunos = alunoDAO.buscarPorProfessor(professor.getId());
        
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno atribuído a você.");
            return;
        }

        System.out.println("\n--- Montar Treino ---");
        // Exibe lista de alunos disponíveis
        alunos.forEach(System.out::println);
        
        // Solicita ID do aluno
        System.out.print("ID do aluno: ");
        int alunoId = lerInt();

        // Solicita dados do treino
        System.out.print("Nome do treino (ex: Treino A - Peito e Tríceps): ");
        String nomeTreino = sc.nextLine().trim();
        System.out.print("Descrição do treino: ");
        String descricao = sc.nextLine().trim();

        // Cria treino com dados informados
        Treino treino = new Treino(0, nomeTreino, alunoId, professor.getId(), descricao);
        
        // Tenta criar treino no banco
        if (!treinoDAO.criar(treino)) {
            System.out.println("Erro ao criar treino.");
            return;
        }

        // Permite adicionar exercícios ao treino
        System.out.println("Treino criado! Agora adicione os exercícios (deixe o nome em branco para parar):");
        
        // Loop para adicionar exercícios
        while (true) {
            System.out.print("\nNome do exercício: ");
            String nomeEx = sc.nextLine().trim();
            
            // Se nomeEx for vazio, sai do loop
            if (nomeEx.isEmpty()) break;

            // Solicita dados do exercício
            System.out.print("Séries: ");
            int series = lerInt();
            
            System.out.print("Repetições: ");
            int reps = lerInt();
            
            System.out.print("Descanso (segundos): ");
            int descanso = lerInt();

            // Cria objeto exercício
            Exercicio ex = new Exercicio(0, treino.getId(), nomeEx, series, reps, descanso);
            
            // Tenta adicionar exercício ao banco
            if (treinoDAO.adicionarExercicio(ex)) {
                System.out.println("✅ Exercício adicionado!");
            }
        }
        
        // Mensagem de conclusão
        System.out.println("✅ Treino '" + nomeTreino + "' salvo com sucesso!");
    }

    /**
     * Método verificarPlanoAluno - Verifica plano e pagamentos de um aluno
     */
    private void verificarPlanoAluno() {
        // Busca alunos do professor
        List<Aluno> alunos = alunoDAO.buscarPorProfessor(professor.getId());
        
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno atribuído a você.");
            return;
        }

        System.out.println("\n--- Verificar Plano de Aluno ---");
        // Exibe lista de alunos disponíveis
        alunos.forEach(System.out::println);
        
        // Solicita ID do aluno
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
        
        // Verifica se tem plano ativo
        if (aluno.getPlanoId() == 0) {
            System.out.println("Sem plano ativo.");
            return;
        }
        
        // Busca e exibe plano
        Plano plano = planoDAO.buscarPorId(aluno.getPlanoId());
        if (plano != null) System.out.println("Plano: " + plano);

        // Busca e exibe último pagamento
        Pagamento ult = pagamentoDAO.verificarUltimoPagamento(alunoId);
        if (ult != null) {
            System.out.println("Vencimento: " + ult.getDataVencimento() + " | Status: " + ult.getStatus().toUpperCase());
        }
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
