package uk.co.scribbleapps.accesscaretest.ui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainActivityTest {

    @Test
    public void returnAnIntTest() {
        assertEquals(30, MainActivity.returnAnInt());
    }

}
