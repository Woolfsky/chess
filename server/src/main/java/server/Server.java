package server;

import service.AdminService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // clear endpoint
        Spark.delete("/db", (request, response) -> {
            if (AdminService.delete()) {
                response.status(200);
            } else {
                String res = response.body();
                response.status(500);
                //return error message JSON object
            }
            return null; // get rid of this once ^^ is implemented
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
