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
        }
    }
}