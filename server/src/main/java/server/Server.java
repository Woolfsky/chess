package server;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
import passoffTests.testClasses.TestModels;
import service.AdminService;
import service.ClientService;
import spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server {

    MemoryDataAccess memoryDataAccess = new MemoryDataAccess();

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.delete("/db", this::delete);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public Object delete(Request request, Response response) throws DataAccessException {
        try {
            new AdminService(memoryDataAccess).delete();
            response.status(200);
            return "{}";
        } catch (Exception ex) {
            response.status(500);
            Map<String, String> m = new HashMap<>();
            m.put("message", ex.getMessage());
            return new Gson().toJson(m);
        }
    }

    public Object register(Request request, Response response) throws DataAccessException {
        Map<String, String> req = new Gson().fromJson(request.body(), Map.class);
        String username = req.get("username");
        String password = req.get("password");
        String email = req.get("email");
        if (username == null || password == null || email == null) {
            response.status(400);
            Map<String, String> m = new HashMap<>();
            m.put("message", "Error: bad request");
            return new Gson().toJson(m);
        }

        try {
            AuthData authData = new ClientService(memoryDataAccess).register(username, password, email);
            response.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            response.status(403);
            Map<String, String> m = new HashMap<>();
            m.put("message", "Error: already taken");
            return new Gson().toJson(m);
        } catch (Exception ex) {
            response.status(500);
            Map<String, String> m = new HashMap<>();
            m.put("message", ex.getMessage());
            return new Gson().toJson(m);
        }
    }

    public Object login(Request request, Response response) throws DataAccessException {
        Map<String, String> req = new Gson().fromJson(request.body(), Map.class);
        String username = req.get("username");
        String password = req.get("password");

        try {
            AuthData authData = new ClientService(memoryDataAccess).login(username, password);
            response.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            response.status(401);
            Map<String, String> m = new HashMap<>();
            m.put("message", "Error: unauthorized");
            return new Gson().toJson(m);
        } catch (Exception ex) {
            response.status(500);
            Map<String, String> m = new HashMap<>();
            m.put("message", ex.getMessage());
            return new Gson().toJson(m);
        }
    }

    public Object logout(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("Authorization");

        try {
            Boolean status = new ClientService(memoryDataAccess).logout(authToken);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            response.status(401);
            Map<String, String> m = new HashMap<>();
            m.put("message", "Error: unauthorized");
            return new Gson().toJson(m);
        } catch (Exception ex) {
            response.status(500);
            Map<String, String> m = new HashMap<>();
            m.put("message", ex.getMessage());
            return new Gson().toJson(m);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }



}
