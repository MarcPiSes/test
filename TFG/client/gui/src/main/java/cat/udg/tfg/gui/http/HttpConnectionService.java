package cat.udg.tfg.gui.http;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnectionService {


    public static HttpURLConnection create(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            return conn;
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO error handling
        }
    }

    public static void setToken(HttpURLConnection conn, String token) {
        conn.setRequestProperty("Authorization", token);
    }

    public static HttpURLConnection post(String apiUrl) {
        HttpURLConnection conn = create(apiUrl);
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static HttpURLConnection get(String apiUrl) {
        HttpURLConnection conn = create(apiUrl);
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static HttpURLConnection delete(String apiUrl) {
        HttpURLConnection conn = create(apiUrl);
        try {
            conn.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static HttpURLConnection put(String apiUrl) {
        HttpURLConnection conn = create(apiUrl);
        try {
            conn.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void addBody(HttpURLConnection conn, String body) {
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResponse(HttpURLConnection conn) {
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getError(HttpURLConnection conn) {
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getResponse(HttpURLConnection conn, Class<?> object) {
        String response = getResponse(conn);
        return new Gson().fromJson(response, object);
    }
}
