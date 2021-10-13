package uk.co.scribbleapps.accesscaretest.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Field;

import uk.co.scribbleapps.accesscaretest.R;
import uk.co.scribbleapps.accesscaretest.data.User;
import uk.co.scribbleapps.accesscaretest.util.ApplicationHelper;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private static final String TAG = "MainActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationHelper applicationHelper = ApplicationHelper.getInstance();
        applicationHelper.addFirstUser(this);

        fragmentManager = getSupportFragmentManager();
        replaceFragment(LoginFragment.newInstance(), false);

        User user = new User("testuser", "Password1", "John",
                "Smith", "john@email.com", "1980", "uri/here");

        Field[] fields = user.getClass().getDeclaredFields();

        Log.d(TAG, "onCreate: test");

        for(Field field: fields) {
            Log.d(TAG, "onCreate: field: " + field);
        }

    }

    /* Fragments that we only want to appear once such as ProfileDetailsRequestFragment
    can be excluded from the backstack
    Static so this can be called from Fragments */
    public static void replaceFragment(Fragment fragment, Boolean addToBackStack) {
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainLayoutMainActivity, fragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(fragment.getTag()) // name can be null
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainLayoutMainActivity, fragment, null)
                    .setReorderingAllowed(true)
                    //.addToBackStack(fragment.toString()) // name can be null
                    .commit();
        }
    }

    public static int returnAnInt() {
        int i = 5;
        int j = 6;
        int k = i*j;
        return k;
    }

}