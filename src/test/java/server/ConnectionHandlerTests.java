package server;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ConnectionHandlerTests {

    @Test
    public void outputFormatTest() {
        String[] testArray = {"TEST","ARG1","ARG2"};
        assertEquals("TEST%ARG1%ARG2%\n", ConnectionHandler.arrayToString(testArray));
    }
}
