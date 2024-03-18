package clientTests;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;
import web.ServerFacade;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        facade.delete();
    }

    @Test
    void register() throws Exception {
        AuthData authData = facade.register("ddsdf", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

}
