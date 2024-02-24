package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import service.AdminService;
import spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Server {

    MemoryDataAccess memoryDataAccess = new MemoryDataAccess();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/test", this::handleTestRequest);
        Spark.delete("/db", this::delete);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public String handleTestRequest(Request request, Response response) {
        return "This is a test response!";
    }

    public Object delete(Request request, Response response) throws DataAccessException {
        System.out.printf("Within server...");
        if (new AdminService(memoryDataAccess).delete()) {
            System.out.printf("about to return a successful response code 200...");
            response.status(200);
            System.out.printf("successful!!");
        } else {
            String res = response.body();
            response.status(500);
            return res;
        }
        return null; // get rid of this once ^^ is implemented
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }



}
