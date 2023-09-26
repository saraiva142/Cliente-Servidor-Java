import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        // 1. Estabelecer a conexão com o Server
        Socket conexao = new Socket("127.0.0.1", 8888);
        System.out.println("Conexão estabelecida com o servidor.");

        // 2. Acessar os fluxos (streams) de entrada e saída de dados
        PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Exibir opções para o usuário
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Login");
            System.out.println("3 - Sair");
            System.out.print("Opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar a nova linha pendente

            if (opcao == 1) {
                // Cadastro de um novo usuário
                System.out.print("Digite o nome de usuário: ");
                String username = scanner.nextLine();
                System.out.print("Digite a senha: ");
                String password = scanner.nextLine();
                System.out.print("Digite o nome completo: ");
                String nomeCompleto = scanner.nextLine();
                System.out.print("Digite o email: ");
                String email = scanner.nextLine();
                System.out.print("Digite o telefone: ");
                String telefone = scanner.nextLine();

                // Enviar solicitação de cadastro ao servidor
                String mensagem = "CADASTRAR " + username + " " + password + " " + nomeCompleto + " " + email + " " + telefone;
                saida.println(mensagem);
            } else if (opcao == 2) {
                // Login de usuário
                System.out.print("Digite o nome de usuário: ");
                String username = scanner.nextLine();
                System.out.print("Digite a senha: ");
                String password = scanner.nextLine();

                // Enviar solicitação de login ao servidor
                String mensagem = "LOGIN " + username + " " + password;
                saida.println(mensagem);
            } else if (opcao == 3) {
                // Encerrar a conexão e sair
                conexao.close();
                System.out.println("Conexão encerrada. Saindo...");
                break;
            } else {
                System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }

            // Receber a resposta do servidor e exibi-la
            String resposta = entrada.readLine();
            System.out.println("Servidor >> " + resposta);
        }
    }
}
