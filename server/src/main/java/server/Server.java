package server;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import passoffTests.testClasses.TestModels;
import service.AdminService;
import service.ClientService;
import service.GameService;
import spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class Server {

    MemoryDataAccess memoryDataAccess = new MemoryDataAccess();

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.delete("/db", this::delete);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public Object delete(Request request, Response response) {
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

    public Object register(Request request, Response response) {
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

    public Object login(Request request, Response response) {
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

    public Object logout(Request request, Response response) {
        String authToken = request.headers("Authorization");

        try {
            new ClientService(memoryDataAccess).logout(authToken);
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

    public Object listGames(Request request, Response response) {
        String authToken = request.headers("Authorization");
        try {
            List<GameData> games = new GameService(memoryDataAccess).listGames(authToken);
            Map<String, List<GameData>> m = new HashMap<>();
            m.put("games", games);
            response.status(200);
            return new Gson().toJson(m);
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

    public Object createGame(Request request, Response response) {
        String authToken = request.headers("Authorization");
        Map<String, String> req = new Gson().fromJson(request.body(), Map.class);
        String gameName = req.get("gameName");
        if (gameName == null) {
            response.status(400);
            Map<String, String> m = new HashMap<>();
            m.put("message", "Error: bad request");
            return new Gson().toJson(m);
        }

        try {
            Integer gameID = new GameService(memoryDataAccess).createGame(authToken, gameName);
            Map<String, Integer> m = new HashMap<>();
            m.put("gameID", gameID);
            response.status(200);
            return new Gson().toJson(m);
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

    public Object joinGame(Request request, Response response) {
        String authToken = request.headers("Authorization");
        Map<String, Object> req = new Gson().fromJson(request.body(), Map.class);
        String playerColor = (String) req.get("playerColor");
        Double gameID = (Double) req.get("gameID");
        int intGameID = gameID.intValue();

        if (gameID.isNaN()) {
            response.status(400);
            Map<String, String> m = new HashMap<>();
            m.put("message", "Error: bad request");
            return new Gson().toJson(m);
        }

        try {
            new GameService(memoryDataAccess).joinGame(authToken, playerColor, Integer.parseInt(String.valueOf(intGameID)));
            Map<String, Integer> m = new HashMap<>();
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Tried to retrieve an authData object for a username not in the system.")) {
                response.status(401);
                Map<String, String> m = new HashMap<>();
                m.put("message", "Error: unauthorized");
                return new Gson().toJson(m);
            } else if (Objects.equals(e.getMessage(), "No game exists")) {
                response.status(400);
                Map<String, String> m = new HashMap<>();
                m.put("message", "Error: bad request");
                return new Gson().toJson(m);
            } else if (Objects.equals(e.getMessage(), "Player position already taken")) {
                response.status(403);
                Map<String, String> m = new HashMap<>();
                m.put("message", "Error: already taken");
                return new Gson().toJson(m);
            }
        } catch (Exception ex) {
            response.status(500);
            Map<String, String> m = new HashMap<>();
            m.put("message", ex.getMessage());
            System.out.printf(ex.getMessage());
            return new Gson().toJson(m);
        }
        return null;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }



}
