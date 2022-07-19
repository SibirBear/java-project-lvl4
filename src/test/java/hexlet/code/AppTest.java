package hexlet.code;

import org.junit.jupiter.api.Test;

import static hexlet.code.App.printResult;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @Test
    void printResultTest() {
        assertTrue(printResult("S"));
    }
}