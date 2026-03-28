package app;

import view.MenuLogin;
import java.util.Scanner;

/**
 * Classe GymFlowSystem - Classe principal do sistema GymFlow
 * 
 * Esta é a porta de entrada da aplicação. Responsabilidades:
 * - Criar o Scanner (para leitura de entrada do usuário)
 * - Inicializar a tela de login
 * - Deixar o programa rodando até o usuário encerrar
 * 
 * Fluxo de execução:
 * 1. main() é chamado quando a aplicação inicia
 * 2. Cria um Scanner para capturar entradas do teclado
 * 3. Instancia MenuLogin e exibe a tela de login
 * 4. MenuLogin controla todo o fluxo de navegação do sistema
 * 5. Scanner é fechado quando o usuário sai
 */
public class GymFlowSystem {

    /**
     * Método main - Ponto de entrada da aplicação
     * 
     * @param args Argumentos da linha de comando (não utilizados neste projeto)
     */
    public static void main(String[] args) {
        // Cria um Scanner para capturar entradas do usuário a partir do teclado (System.in)
        Scanner sc = new Scanner(System.in);
        
        // Instancia e exibe o MenuLogin, que controla todo o fluxo da aplicação
        new MenuLogin(sc).exibir();
        
        // Fecha o Scanner para liberar recursos após o usuário encerrar
        sc.close();
    }
}
