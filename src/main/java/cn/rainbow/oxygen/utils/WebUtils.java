package cn.rainbow.oxygen.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WebUtils {

    public static String sendGet(String url) throws IOException {
        StringBuilder result = new StringBuilder();

        URLConnection connection = getURLConnection(url);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    public static URLConnection getURLConnection(String url) throws IOException {
        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        connection.setDoOutput(true);
        connection.setReadTimeout(99781);
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.76");
        connection.connect();
        return connection;
    }
}
