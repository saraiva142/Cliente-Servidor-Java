import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        // 1. Estabelecer a conexão com o Server
        Socket conexao = new Socket("127.0.0.1", 8888);
        // 2. Acessar os fluxos (streams) de entrada e saída de dados
        PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        while (true) {

            // 3.Client vai enviar mensagem para o Server
            System.out.println("Cliente >> ");
            String msg = new Scanner(System.in).nextLine();
            saida.println(msg);

            // 4. Receber os dados do Server
            String msg2 = entrada.readLine();
            System.out.println(msg2);

            // 5. Exemplo de autenticação
            if (msg.startsWith("LOGIN")) {
                String[] parts = msg.split(" ");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    System.out.println("Tentando autenticar...");
                } else {
                    System.out.println("Formato incorreto. Use: LOGIN nome senha");
                }
            }

            // 6. Exemplo de cadastro de um novo usuário
            if (msg.startsWith("CADASTRAR")) {
                String[] parts = msg.split(" ");
                if (parts.length == 6) {
                    String username = parts[1];
                    String password = parts[2];
                    String nome_completo = parts[3];
                    String email = parts[4];
                    String telefone = parts[5];
                    System.out.println("Cadastro de usuário em andamento...");
                } else {
                    System.out.println("Formato incorreto. Use: CADASTRAR nome senha nome_completo email telefone");
                }
            }
        }
    }
}