package uk.co.scribbleapps.accesscaretest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.security.Signature;
import java.util.Objects;
import java.util.concurrent.Executor;

import uk.co.scribbleapps.accesscaretest.R;
import uk.co.scribbleapps.accesscaretest.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragmentTAG";

    private EditText editTextPassword;
    private EditText editTextUserName;
    private LoginViewModel viewModel;
    private CancellationSignal cancellationSignal;
    private BiometricPrompt.AuthenticationCallback  authenticationCallback;
    private LoginViewModel loginViewModel;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Button biometricLoginButton;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    // Show the options menu
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Inflate the refresh menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    // Set the refresh icon to invisible
    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_refresh).setVisible(false);
    }

    // In our menu, hide the back button, and set the title
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Prevents two fragments being shown at once
        if (container != null) {
            container.removeAllViews();
        }

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Access Care Test");

        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    // Get our references to the Layout Views, and set a login button listener
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: enter");

        // Get a reference to the ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Get references to views
        editTextUserName = view.findViewById(R.id.loginUsernameTextView);
        editTextPassword = view.findViewById(R.id.loginPasswordTextView);
        Button buttonLogin = view.findViewById(R.id.loginButton);

        // Set login button listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                login(username, password);
            }
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        biometricLoginButton = view.findViewById(R.id.loginButtonFingerprint);

        if(checkBiometricSupport()) {

            executor = ContextCompat.getMainExecutor(requireContext());
            biometricPrompt = new BiometricPrompt((FragmentActivity) requireContext(),
                    executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(requireContext(),
                            "Authentication error: " + errString, Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(requireContext(),
                            "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(requireContext(), "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build();

            biometricLoginButton.setOnClickListener(v -> {
                biometricPrompt.authenticate(promptInfo);
            });

        } else {
            biometricLoginButton.setVisibility(View.GONE);
        }
    }

    /* On selecting login - check is username and password is correct - if so, check whether a profile
    exists - it does, then open ProfileFragment, otherwise open ProfileDetailsRequestFragment */
    public void login(String username, String password) {
        if (viewModel.checkLoginDetails(username, password)) {
            // Reset the username and password so when the back button is pressed, they can't be viewed again
            editTextUserName.setText("");
            editTextPassword.setText("");
            if (viewModel.checkProfileDetailsArePresent(username)) {
                MainActivity.replaceFragment(ProfileFragment.newInstance(), true);
            } else {
                // Set the current username and password to SharedPreferences so we can
                // use them in ProfileDetailsRequestFragment
                viewModel.setCurrentUsernameAndPassword(username, password);
                MainActivity.replaceFragment(ProfileDetailsRequestFragment.newInstance(), true);
            }
        } else {
            Toast.makeText(getContext(), "Incorrect details", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private boolean checkBiometricSupport() {
        /*if(!viewModel.keyguardManagerAndPermissionCheck()) {
            notifyUser("Fingerprint has not been enabled in settings.");
            return false;
        }
        else if(!viewModel.deviceHasFingerprintScanner()) {
            notifyUser("This device does not have a fingerprint scanner.");
            return false;
        }
        else {
            return true;
        }*/
        return true;
    }

    private void notifyUser(String message) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}