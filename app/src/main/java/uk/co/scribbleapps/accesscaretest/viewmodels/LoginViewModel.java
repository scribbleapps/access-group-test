package uk.co.scribbleapps.accesscaretest.viewmodels;

import androidx.lifecycle.ViewModel;

import uk.co.scribbleapps.accesscaretest.data.User;
import uk.co.scribbleapps.accesscaretest.util.ApplicationHelper;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModelTAG";
    private final ApplicationHelper applicationHelper = ApplicationHelper.getInstance();

    public LoginViewModel() {}

    public Boolean checkLoginDetails(String username, String password) {
        User user = applicationHelper.getUser(username.trim());
        return user != null && user.getPassword().equals(password.trim());
    }

    public Boolean checkProfileDetailsArePresent(String username) {
        return applicationHelper.isProfilePresent(username);
    }

    public void setCurrentUsernameAndPassword(String username, String password) {
        applicationHelper.setCurrentUsernameAndPassword(username, password);
    }

}