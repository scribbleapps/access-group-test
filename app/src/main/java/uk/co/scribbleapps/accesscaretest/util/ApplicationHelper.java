package uk.co.scribbleapps.accesscaretest.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import uk.co.scribbleapps.accesscaretest.data.User;

public class ApplicationHelper extends Application {

    private static final String TAG = "ApplicationHelperTAG";
    private SharedPreferences prefs;
    private static ApplicationHelper applicationHelperSingleton = null;
    public static final String CURRENT_PASSWORD = "currentPassword";
    public static final String CURRENT_USERNAME = "currentUsername";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: enter");
    }

    // Ensures only one instance of ApplicationHelper is used across the app
    public static ApplicationHelper getInstance() {
        if (applicationHelperSingleton == null) {
            applicationHelperSingleton = new ApplicationHelper();
        }
        return applicationHelperSingleton;
    }

    // Used above in the singleton constructor
    public ApplicationHelper() {}

    // If our first user is not already present, add just the username and password
    public void addFirstUser(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (getUser("testuser") == null) {
            User user = new User("testuser", "Password1", "", "", "", "", "");
            Gson gson = new Gson();
            prefs.edit().putString(user.getUsername(), gson.toJson(user)).apply();
        }
    }

    // Retrieves a user from SharedPrefs based on their username
    public User getUser(String username) {
        User user;
        Gson gson = new Gson();
        Type userType = new TypeToken<User>(){}.getType();
        try {
            user = gson.fromJson(prefs.getString(username, ""), userType);
            return user;
        } catch (JsonIOException e) {
            Log.e(TAG, "getUser: JsonIOException: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "getUser: JsonSyntaxException: " + e.getMessage());
        }
        return null;
    }

    // Checks to see if a full profile is present, by checking the saved user details
    // for the given username
    public Boolean isProfilePresent(String username) {
        User user;
        Gson gson = new Gson();
        Type userType = new TypeToken<User>(){}.getType();
        try {
            user = gson.fromJson(prefs.getString(username, ""), userType);
            return !user.getFirstName().equals("");
        } catch (JsonIOException e) {
            Log.e(TAG, "isProfilePresent: JsonIOException: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "isProfilePresent: JsonSyntaxException: " + e.getMessage());
        }
        return false;
    }

    // Allows us to later call these values to get the full User object from SharedPrefs
    public void setCurrentUsernameAndPassword(String username, String password) {
        prefs.edit().putString(CURRENT_USERNAME, username).apply();
        prefs.edit().putString(CURRENT_PASSWORD, password).apply();
    }

    public String getCurrentUsername() {
        return prefs.getString(CURRENT_USERNAME, "");
    }

    public String getCurrentPassword() {
        return prefs.getString(CURRENT_PASSWORD, "");
    }

    // Deletes the current User object for the given username, and replaces it with the new object
    public void replaceUser(User newUser) {
        prefs.edit().remove(newUser.getUsername()).apply();
        Gson gson = new Gson();
        prefs.edit().putString(newUser.getUsername(), gson.toJson(newUser)).apply();
    }
}
