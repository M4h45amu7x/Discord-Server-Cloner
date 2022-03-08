package xyz.mahasamut.DiscordServerCloner.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Base64;

public class StringUtils {

    public static String getByteArrayFromImageURL(String url) throws Exception {
        HttpURLConnection connection = HttpUtils.request(url, "GET");
        InputStream input = connection.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = input.read(buffer, 0, buffer.length)) != -1) {
            output.write(buffer, 0, read);
        }
        output.flush();
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

}
