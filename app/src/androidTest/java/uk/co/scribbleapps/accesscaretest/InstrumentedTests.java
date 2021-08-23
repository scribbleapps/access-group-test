package uk.co.scribbleapps.accesscaretest;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.co.scribbleapps.accesscaretest.ui.MainActivity;
import uk.co.scribbleapps.accesscaretest.viewmodels.LoginViewModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTests {

    private LoginViewModel loginViewModel;
    private Context appContext;
    private String stringToBetyped;

    @Test
    public void useAppContext() {
        // Context of the app under test
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("uk.co.scribbleapps.accesscaretest", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    // Before we run any tests, open MainActivity
    @Before
    public void setUp() {
        loginViewModel = new LoginViewModel();
        ActivityScenario scenario = rule.getScenario();
        stringToBetyped = "Espresso";
    }

    @Test
    public void twoEqualsTwo() {
        assertEquals("2", "2");
    }

    @Test
    public void checkLoginDetailsReturnsCorrectly() {
        assertEquals(true, loginViewModel.checkLoginDetails("u", "p"));
        assertEquals(false, loginViewModel.checkLoginDetails("bob", "sandwiches"));
        assertEquals(false, loginViewModel.checkLoginDetails("U", "P"));
    }

    @Test
    public void checkLoginButtonDisplayed() {
        onView(withId(R.id.loginButton))            // withId(R.id.loginButton) is a ViewMatcher
                .check(matches(isDisplayed())); // matches(isDisplayed()) is a ViewAssertion
    }

    @Test
    public void enteredLoginUsernameShowing() {
        // Type text
        onView(withId(R.id.loginUsernameTextView))
                .perform(typeText(stringToBetyped), closeSoftKeyboard());

        // Check that the text was changed
        onView(withId(R.id.loginUsernameTextView))
                .check(matches(withText(stringToBetyped)));
    }

}
