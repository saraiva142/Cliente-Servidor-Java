import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject; // Certifique-se de adicionar a biblioteca JSON correspondente ao seu projeto

public class YahooFinanceAPI {
    // Método para obter o preço das ações da Apple (AAPL)
    public static double getStockPrice() {
        try {
            // URL da API do Yahoo Finance para obter o preço das ações da Apple (AAPL)
            URL url = new URL("https://query1.finance.yahoo.com/v7/finance/quote?symbol=AAPL");

            // Abre uma conexão HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configura o método da requisição como GET
            conn.setRequestMethod("GET");

            // Configura o tempo máximo de espera pela resposta
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Obtém o código de status da resposta HTTP
            int status = conn.getResponseCode();

            // Lê a resposta
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Fecha a conexão e o BufferedReader
            in.close();
            conn.disconnect();

            // Exibe a resposta
            System.out.println("HTTP Status Code: " + status);
            System.out.println("Response: " + content.toString());

            // Analisa a resposta JSON para obter o preço das ações
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONObject quote = jsonResponse.getJSONObject("quoteResponse").getJSONArray("result").getJSONObject(0);
            double stockPrice = quote.getDouble("regularMarketPrice");

            // Retorna o preço das ações
            return stockPrice;

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Retorna um valor negativo para indicar erro
        }
    }

    public static void main(String[] args) {
        double price = getStockPrice();
        if (price >= 0) {
            System.out.println("Preço das ações da Apple (AAPL): " + price);
        } else {
            System.out.println("Erro ao obter o preço das ações.");
        }
    }
}
