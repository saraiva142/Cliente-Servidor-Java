import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws Exception {
        // 1. Escutar a porta de serviço 8888
        ServerSocket server = new ServerSocket(8888);
        System.out.println("Servidor aguardando conexões...");
        
        while (true) {
            // 2. Aguardar conexão do Cliente
            Socket conexao = server.accept();
            System.out.println("Cliente conectado: " + conexao.getInetAddress().getHostAddress());
            
            // 3. Obter os fluxos de entrada e saída
            PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            // 4. Configurar a conexão com o banco de dados SQLite
            String url = "jdbc:sqlite:C:\\Users\\Elisio da Silva\\docker\\base.db"; // Caminho absoluto para o banco de dados
            Connection connection = DriverManager.getConnection(url);

            while (true) {
                // 5. Receber os dados do cliente
                String msg = entrada.readLine();
                System.out.println("Cliente >> " + msg);

                // 6. Exemplo de inserção de um novo usuário na tabela
                if (msg.startsWith("CADASTRAR")) {
                    String[] parts = msg.split(" ");
                    if (parts.length == 6) {
                        String username = parts[1];
                        String password = parts[2];
                        String nome_completo = parts[3];
                        String email = parts[4];
                        String telefone = parts[5];
                        insertUser(connection, username, password, nome_completo, email, telefone);
                        saida.println("Cadastro de usuário realizado com sucesso.");
                    } else {
                        saida.println("Formato incorreto. Use: CADASTRAR nome senha nome_completo email telefone");
                    }
                }
                // 7. Autenticação do usuário
                else if (msg.startsWith("LOGIN")) {
                    String[] parts = msg.split(" ");
                    if (parts.length == 3) {
                        String username = parts[1];
                        String password = parts[2];
                        if (authenticateUser(connection, username, password)) {
                            saida.println("Login Successful");
                        } else {
                            saida.println("Falha na autenticação. Verifique nome de usuário e senha.");
                        }
                    } else {
                        saida.println("Formato incorreto. Use: LOGIN nome senha");
                    }
                } else {
                    saida.println("Comando não reconhecido.");
                }
            }
        }
    }

    // Método para inserir um novo usuário na tabela
    private static void insertUser(Connection connection, String username, String password, String nome_completo, String email, String telefone) {
        String sql = "INSERT INTO user (name, passworld, nome_completo, email, telefone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, nome_completo);
            statement.setString(4, email);
            statement.setString(5, telefone);
            statement.executeUpdate();
            System.out.println("Cadastro de novo usuário realizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar novo usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para autenticar um usuário na tabela
    private static boolean authenticateUser(Connection connection, String username, String password) {
        String sql = "SELECT * FROM user WHERE name = ? AND passworld = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Retorna verdadeiro se um registro correspondente for encontrado
        } catch (SQLException e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
