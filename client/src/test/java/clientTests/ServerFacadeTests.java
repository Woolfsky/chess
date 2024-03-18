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

    @Test
    void registerBad() throws Exception {
        facade.register("ddsdf", "password", "p1@email.com");
        assertThrows(Exception.class, () -> {
            facade.register("ddsdf", "password", "p1@email.com");
        });
    }

    @Test
    void login() throws Exception {
        facade.register("testGuy", "password", "testguy@email.com");
        AuthData authData = facade.login("testGuy", "password");
        assertTrue(authData.authToken().length() > 10);
        // note, this allows two authDatas for one user... fix this bug
    }

    @Test
    void loginBad() {
        assertThrows(Exception.class, () -> {
            facade.login("ddsdf", "password");
        });
    }

    @Test
    void logout() throws Exception {
        AuthData auth = facade.register("testGuy", "password", "testguy@email.com");
        Object o = facade.logout(auth);
        assertEquals(o.toString(), "{}");
    }

    @Test
    void logoutBad() {
        AuthData fakeAuth = new AuthData("fake", "fake");
        assertThrows(Exception.class, () -> {facade.logout(fakeAuth);});
    }

}
