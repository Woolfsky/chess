package serviceTests;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import server.*;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class FirstTest {

    static Server server;
    static URL url;

    @BeforeAll
    static void startServer() throws Exception {
        server = new Server();
        int port = server.run(8080);
        url = new URL("http://localhost:" + port + "/db");
    }

    @BeforeEach
    void clear() throws ResponseException {
        this.makeRequest("DELETE", "/db", null, null);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testGetRequest() throws ResponseException {
        this.makeRequest("GET", "/test", null, null);
    }

    @Test
    public void clearEmptyData() throws ResponseException {
        this.makeRequest("DELETE", "/db", null, null);
    }

    @Test
    public void registerUsers() throws ResponseException {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("username","CoderGuy");
        m.put("password","123");
        m.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m, Object.class);

        HashMap<String, String> m2 = new HashMap<String, String>();
        m2.put("username","CoderGirl");
        m2.put("password","123");
        m2.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m2, Object.class);
    }

    @Test
    public void clearPopulatedData() throws ResponseException {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("username","CoderGuy");
        m.put("password","123");
        m.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m, Object.class);

        this.makeRequest("DELETE", "/db", null, null);
    }

    @Test
    public void registerExistingUser() throws ResponseException {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("username","CoderGuy");
        m.put("password","123");
        m.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m, Object.class);

        HashMap<String, String> m2 = new HashMap<String, String>();
        m2.put("username","CoderGuy");
        m2.put("password","123");
        m2.put("email","coderemail@emails.com");
        assertThrows(ResponseException.class, () -> {
            this.makeRequest("POST", "/user", m2, Object.class);
        });
    }

    @Test
    public void loginExistingUser() throws ResponseException {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("username","CoderGuy");
        m.put("password","123");
        m.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m, Object.class);

        HashMap<String, String> m2 = new HashMap<String, String>();
        m2.put("username","CoderGuy");
        m2.put("password","123");
        this.makeRequest("POST","/session", m2, Object.class);
    }

    @Test
    public void loginAttemptNonRegisteredUser() throws ResponseException {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("username","CoderGuy");
        m.put("password","123");
        m.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m, Object.class);

        HashMap<String, String> m2 = new HashMap<String, String>();
        m2.put("username","CoderGal");
        m2.put("password","120000");
        assertThrows(ResponseException.class, () -> {
            this.makeRequest("POST","/session", m2, Object.class);
        });
    }

    @Test
    public void incorrectPassword() throws ResponseException {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("username","CoderGuy");
        m.put("password","123");
        m.put("email","coderemail@emails.com");
        this.makeRequest("POST", "/user", m, Object.class);

        HashMap<String, String> m2 = new HashMap<String, String>();
        m2.put("username","CoderGuy");
        m2.put("password","120000");
        assertThrows(ResponseException.class, () -> {
            this.makeRequest("POST","/session", m2, Object.class);
        });
    }











    //
    //
    //
    // code from PetShop for testing, below
    //
    //
    //

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            var serverUrl = "http://localhost:8080";
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public class ResponseException extends Exception {
        final private int statusCode;

        public ResponseException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }

        public int StatusCode() {
            return statusCode;
        }
    }

}
