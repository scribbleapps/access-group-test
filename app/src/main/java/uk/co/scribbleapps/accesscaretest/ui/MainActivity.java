package uk.co.scribbleapps.accesscaretest.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import uk.co.scribbleapps.accesscaretest.R;
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

}