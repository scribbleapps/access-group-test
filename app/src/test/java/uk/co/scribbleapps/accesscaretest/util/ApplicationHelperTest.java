package uk.co.scribbleapps.accesscaretest.util;

import org.junit.Test;

import uk.co.scribbleapps.accesscaretest.ui.MainActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplicationHelperTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testingJaCoCoWorksTest() {
        assertTrue(ApplicationHelper.testingJaCoCoWorks());
    }

}
