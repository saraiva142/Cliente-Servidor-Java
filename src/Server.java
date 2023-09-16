import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        // 4. Configurar a conexão com o banco de dados SQLite
        String url = "jdbc:sqlite:C:\\Users\\Elisio da Silva\\docker\\base.db"; // Caminho absoluto para o banco de dados
        Connection connection = DriverManager.getConnection(url);

        //Teste de Conexão
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return; // Saia do programa em caso de erro na conexão
        }

        

        while (true) {
            // 4. Receber os dados do cliente
            String msg = entrada.readLine();
            System.out.println(msg);
            /*  5. Enviar resposta para o cliente
            System.out.println("Servidor >> ");
            String msg2 = new Scanner(System.in).nextLine();
            saida.println(msg2);



            // 6. Exemplo de inserção de um novo usuário na tabela
            if (msg.startsWith("INSERIR")) {
                String[] parts = msg.split(" ");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    insertUser(connection, username, password);
                }
            }*/

            // 5. Exemplo de inserção de um novo usuário na tabela
            if (msg.startsWith("INSERIR")) {
                String[] parts = msg.split(" ");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    insertUser(connection, username, password);
                    saida.println("Novo usuário inserido com sucesso.");
                } else {
                    saida.println("Formato incorreto. Use: INSERIR nome senha");
                }
            } else {
                // 6. Enviar resposta para o cliente
                System.out.print("Servidor >> ");
                String msg2 = new BufferedReader(new InputStreamReader(System.in)).readLine();
                saida.println(msg2);
            }

        }
    }

    // Método para inserir um novo usuário na tabela
    private static void insertUser(Connection connection, String username, String password) {
        String sql = "INSERT INTO user (name, passworld) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("Novo usuário inserido com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir novo usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



