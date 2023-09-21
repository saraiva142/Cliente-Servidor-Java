import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Lógica de atendimento ao cliente aqui
            // Leia mensagens do cliente, processe-as e envie respostas

            // Exemplo simples: ecoar mensagens do cliente de volta para ele
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Mensagem do cliente: " + message);
                out.println("Servidor: " + message);
            }

            // Encerre a conexão com o cliente quando ele sair
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
