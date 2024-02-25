package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.AuthData;
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
import java.util.Map;
import java.util.UUID;

public class Server {

    MemoryDataAccess memoryDataAccess = new MemoryDataAccess();

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.get("/test", this::handleTestRequest);
        Spark.post("/test2", this::handleTestRequestPOST);

        Spark.delete("/db", this::delete);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
//        Spark.delete("/session", this::logout);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public String handleTestRequest(Request request, Response response) {
        return "This is a test response!";
    }

    public String handleTestRequestPOST(Request request, Response response) {
        String req = new Gson().fromJson(request.body(), String.class);
        return "Your parameter was " + req;
    }

    public Object delete(Request request, Response response) throws DataAccessException {
        if (new AdminService(memoryDataAccess).delete()) {
            response.status(200);
            return "{}";
        } else {
            String res = response.body();
            response.status(500);
            return new Gson().toJson(res);
        }
    }

    public Object register(Request request, Response response) throws DataAccessException {
        Map<String, String> req = new Gson().fromJson(request.body(), Map.class);
        String username = req.get("username");
        String password = req.get("password");
        String email = req.get("email");

        AuthData authData = new ClientService(memoryDataAccess).register(username, password, email);
        if (authData != null) {
            response.status(200);
            return new Gson().toJson(authData);
        } else {
            String res = response.body();
            response.status(403);
            return new Gson().toJson(res);
        }

    }

    public Object login(Request request, Response response) throws DataAccessException {
        Map<String, String> req = new Gson().fromJson(request.body(), Map.class);
        String username = req.get("username");
        String password = req.get("password");

        AuthData authData = new ClientService(memoryDataAccess).login(username, password);

        if (authData != null) {
            response.status(200);
            return new Gson().toJson(authData);
        } else {
            String res = response.body();
            response.status(500);
            return new Gson().toJson(res);
        }

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }



}
