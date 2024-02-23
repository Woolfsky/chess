package serviceTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import static org.junit.jupiter.api.Assertions.fail;

public class FirstTest {
    @Test
    public void MyTest1() {
        int i = 7;
        Assertions.assertTrue(i > 6);
        Assertions.assertEquals(i, 7);
//        fail("bad");
    }

    @Test
    public void ErrorTest() {
        Assertions.assertThrows(Exception.class, () -> {
            throw new Exception();}
        );
    }

}
