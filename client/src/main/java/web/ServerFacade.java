package web;

import com.google.gson.Gson;
import model.AuthData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;

public class ServerFacade {
    Integer port;

    public ServerFacade(Integer p) {
        port = p;
    }

    public Object delete() throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, Object.class);
        }
    }

    public AuthData register(String username, String password, String email) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = Map.of("username", username, "password", password, "email", email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, AuthData.class);
        }
    }

    public AuthData login(String username, String password) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = Map.of("username", username, "password", password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, AuthData.class);
        }
    }

    public Object logout(AuthData auth) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth.getAuthToken());

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, Object.class);
        }
    }

    public Object createGame(AuthData auth, String gameName) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth.getAuthToken());

        // Write out the body
        var body = Map.of("gameName", gameName);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, Object.class);
        }
    }


}
