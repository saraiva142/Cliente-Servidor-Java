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


        // Após a criação da conexão TCP com o servidor
        DatagramSocket udpSocket = new DatagramSocket(12345); // Porta para receber dados do servidor




        while (true) {

            // 3.Client vai enviar mensagem para o Server
            System.out.println("Para obter dados financeiros, digite 'GET_DATA'");
            System.out.println("Para Cadastrar Escreva: 'CADASTRAR usuario senha nome/sobrenome email telefone'");
            System.out.println("Para fazer Login Escreva: 'LOGIN usuario senha'");
            System.out.println("Cliente >> ");
            String msg = new Scanner(System.in).nextLine();
            saida.println(msg);

            // 4. Receber os dados do Server
            String msg2 = entrada.readLine();
            System.out.println(msg2);

            if (msg.equals("GET_DATA")) {
                // Aguarde a mensagem UDP do servidor
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocket.receive(receivePacket);
                String udpMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        
                // Processar os dados financeiros recebidos (exemplo: exibir na tela)
                System.out.println("Dados Financeiros Recebidos: " + udpMessage);
            } else {
                // Receba a resposta do servidor via TCP (opcional)
                String tcpResponse = entrada.readLine();
                System.out.println("Resposta do servidor: " + tcpResponse);
            }

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