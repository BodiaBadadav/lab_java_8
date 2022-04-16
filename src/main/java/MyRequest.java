import java.io.*;
import java.net.*;
/** Класс для анализа и получа=ения ссылок */
public class MyRequest {
    final static int timeout = 1000;
    final static int readTimeout = 1000;
    final static String requestMethod = "GET";
    final static String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36";

    public static String httpsRequest(String urlStr) throws IOException {
        final URL url = new URL(urlStr);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(readTimeout);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("User-Agent", userAgent);

        try (final InputStream inputStream = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        }

    }
}