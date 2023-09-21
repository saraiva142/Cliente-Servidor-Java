import java.net.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

        
            Socket clientSocket = server.accept();
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

            // Crie uma nova thread para lidar com a conexão do cliente
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            Thread thread = new Thread(clientHandler);
            thread.start();
        

        // Após a criação da conexão TCP com o cliente
        DatagramSocket udpSocket = new DatagramSocket();
        
        while (true) {
            // 4. Receber os dados do cliente
            String msg = entrada.readLine();
            System.out.println(msg);

            if (msg.equals("GET_DATA")) {
                // Obtenha o preço das ações da Apple (AAPL) usando a classe YahooFinanceAPI
                double stockPrice = YahooFinanceAPI.getStockPrice();

                if (stockPrice >= 0) {
                    // Formate a mensagem UDP e envie-a para o cliente (substitua pelo endereço IP e porta do cliente)
                    InetAddress clientAddress = clientSocket.getInetAddress();
                    int clientPort = 12345; // Porta do cliente
                    String udpMessage = "AAPL: " + stockPrice;
                    byte[] sendData = udpMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    udpSocket.send(sendPacket);

                    // Envie também a mensagem via TCP para confirmação (opcional)
                    System.out.println("Dados financeiros enviados: " + udpMessage);
                } else {
                    // Se ocorrer um erro ao obter o preço das ações
                    System.out.println("Erro ao obter o preço das ações.");
                }
            }

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
            }
            // 6. Autenticação do usuário
            if (msg.startsWith("LOGIN")) {
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
                    saida.println("Login Failed. Use: LOGIN nome senha");
                }
            } else if (msg.startsWith("CADASTRAR")) { //Cadastrar informações do usuário
                String[] parts = msg.split(" ");
                    if (parts.length == 6) {
                        String username = parts[1];
                        String password = parts[2];
                        String nome_completo = parts[3];
                        String email = parts[4];
                        String telefone = parts[5];
                        registerUser(connection, username, password, nome_completo, email, telefone);
                        saida.println("Cadastro de usuário realizado com sucesso.");
                    } else {
                        saida.println("Formato incorreto. Use: CADASTRAR nome senha nome_completo email telefone");
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

    // Método para cadastrar um novo usuário na tabela
    private static void registerUser(Connection connection, String username, String password, String nome_completo, String email, String telefone) {
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