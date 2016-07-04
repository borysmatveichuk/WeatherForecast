package net.borkiss.weatherforecast;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Date d = new Date(1467651600*1000L);
        assertEquals(d.getTime(), 1467651600*1000L);
    }
}