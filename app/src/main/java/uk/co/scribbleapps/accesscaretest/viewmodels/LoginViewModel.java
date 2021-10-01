package uk.co.scribbleapps.accesscaretest.viewmodels;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import uk.co.scribbleapps.accesscaretest.data.User;
import uk.co.scribbleapps.accesscaretest.util.ApplicationHelper;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModelTAG";
    private final ApplicationHelper applicationHelper = ApplicationHelper.getInstance();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

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

    @RequiresApi(api = Build.VERSION_CODES.P)
    public boolean keyguardManagerAndPermissionCheck() {
        KeyguardManager keyguardManager =
                (KeyguardManager) getApplication().getSystemService(Context.KEYGUARD_SERVICE);
        boolean biometricPermission = ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED;
        return keyguardManager.isKeyguardSecure() && biometricPermission;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean deviceHasFingerprintScanner() {
        return getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
    }





}