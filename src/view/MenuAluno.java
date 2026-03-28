package view;

import dao.PagamentoDAO;
import dao.PlanoDAO;
import dao.TreinoDAO;
import java.util.List;
import java.util.Scanner;
import model.Aluno;
import model.Pagamento;
import model.Plano;
import model.Treino;

/**
 * Classe MenuAluno - Menu principal para alunos/clientes da academia
 * 
 * Responsabilidades:
 * - Exibir dados pessoais do aluno
 * - Mostrar plano de assinatura e pagamentos
 * - Exibir treinos designados pelo professor
 * 
 * Atributos:
 * - aluno: Objeto do usuário logado
 * - sc: Scanner para capturar entradas
 * - planoDAO, pagamentoDAO, treinoDAO: DAOs para acesso aos dados
 */
public class MenuAluno {

    private Aluno aluno;
    private Scanner sc;
    private PlanoDAO planoDAO = new PlanoDAO();
    private PagamentoDAO pagamentoDAO = new PagamentoDAO();
    private TreinoDAO treinoDAO = new TreinoDAO();

    /**
     * Construtor MenuAluno
     * @param aluno Aluno logado
     * @param sc Scanner para entradas
     */
    public MenuAluno(Aluno aluno, Scanner sc) {
        this.aluno = aluno;
        this.sc = sc;
    }

    /**
     * Método exibir - Exibe o menu principal do aluno
     * 
     * Menu com opções:
     * 1. Ver perfil (dados pessoais)
     * 2. Ver plano e pagamentos
     * 3. Ver treinos passe pelo professor
     * 0. Sair
     */
    public void exibir() {
        int opcao;
        do {
            // Exibe o menu de opções
            System.out.println("\n══════════════════════════════");
            System.out.println("  Bem-vindo, " + aluno.getNome() + "!");
            System.out.println("  [1] Meu Perfil");
            System.out.println("  [2] Meu Plano & Pagamentos");
            System.out.println("  [3] Meus Treinos");
            System.out.println("  [0] Sair");
            System.out.println("══════════════════════════════");
            System.out.print("Opção: ");

            // Lê a opção do usuário
            opcao = lerInt();
            
            // Processa a opção selecionada
            switch (opcao) {
                case 1 -> verPerfil();
                case 2 -> verPlano();
                case 3 -> verTreinos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Método verPerfil - Exibe os dados pessoais do aluno
     */
    private void verPerfil() {
        System.out.println("\n--- Meu Perfil ---");
        System.out.println("Nome  : " + aluno.getNome());
        System.out.println("CPF   : " + aluno.getCpf());
        System.out.println("Email : " + aluno.getEmail());
    }

    /**
     * Método verPlano - Exibe o plano atual e informações de pagamentos
     */
    private void verPlano() {
        System.out.println("\n--- Meu Plano ---");
        
        // Verifica se o aluno tem plano ativo
        if (aluno.getPlanoId() == 0) {
            System.out.println("Nenhum plano ativo no momento.");
            return;
        }
        
        // Busca e exibe o plano
        Plano plano = planoDAO.buscarPorId(aluno.getPlanoId());
        if (plano != null) System.out.println(plano);

        // Busca e exibe o último pagamento
        Pagamento ult = pagamentoDAO.verificarUltimoPagamento(aluno.getId());
        if (ult != null) {
            System.out.println("\nÚltimo pagamento: " + ult.getDataPagamento());
            System.out.println("Vencimento      : " + ult.getDataVencimento());
            System.out.println("Status          : " + ult.getStatus().toUpperCase());
        }
    }

    /**
     * Método verTreinos - Exibe todos os treinos do aluno
     * 
     * Busca treinos do aluno com seus exercícios e exibe de forma formatada
     */
    private void verTreinos() {
        System.out.println("\n--- Meus Treinos ---");
        
        // Busca todos os treinos do aluno (já carregados com exercícios)
        List<Treino> treinos = treinoDAO.buscarPorAluno(aluno.getId());
        
        // Se não houver treinos, exibe mensagem
        if (treinos.isEmpty()) {
            System.out.println("Nenhum treino cadastrado ainda.");
            return;
        }
        
        // Exibe cada treino com seus exercícios
        for (Treino t : treinos) {
            System.out.println("\n" + t);
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
