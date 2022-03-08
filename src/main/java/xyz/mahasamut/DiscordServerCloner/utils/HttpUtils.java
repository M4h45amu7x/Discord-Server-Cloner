package xyz.mahasamut.DiscordServerCloner.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author M4h45amu7x
 */
public class HttpUtils {

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0";

    public static HttpURLConnection request(String url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestMethod(method);
        connection.addRequestProperty("User-Agent", USER_AGENT);
        connection.setInstanceFollowRedirects(true);
        connection.setDoOutput(true);

        return connection;
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = request(url, "GET");

        if (headers != null) {
            for (String key : headers.keySet())
                connection.addRequestProperty(key, headers.get(key));
        }

        return getStringFromSteam(connection.getInputStream());
    }

    public static String post(String url, Map<String, String> headers, String body) throws IOException {
        HttpURLConnection connection = request(url, "POST");

        if (headers != null) {
            for (String key : headers.keySet())
                connection.addRequestProperty(key, headers.get(key));
        }

        if (body != null) {
            byte[] out = body.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = connection.getOutputStream();
            stream.write(out);
        }

        return getStringFromSteam(connection.getInputStream());
    }

    public static String delete(String url, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = request(url, "DELETE");

        if (headers != null) {
            for (String key : headers.keySet())
                connection.addRequestProperty(key, headers.get(key));
        }

        return getStringFromSteam(connection.getInputStream());
    }

    public static String getStringFromSteam(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();

        while (line != null) {
            stringBuilder.append(line).append(System.lineSeparator());
            line = reader.readLine();
        }
        reader.close();

        return stringBuilder.toString();
    }

}