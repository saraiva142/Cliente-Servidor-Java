import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws Exception {
        // 1. Escutar a porta de serviço 8888
        ServerSocket server = new ServerSocket(8888);
        // 2. Aguardar conexão do Cliente
        Socket conexao = server.accept();
        // 3. Obter os fluxos de entrada e saida
        PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        while (true) {
            // 4. Receber os dados do cliente
            String msg = entrada.readLine();
            System.out.println(msg);
            // 5. Enviar resposta para o cliente
            System.out.println("Servidor >> ");
            String msg2 = new Scanner(System.in).nextLine();
            saida.println(msg2);
        }
    }
}
