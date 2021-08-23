package uk.co.scribbleapps.accesscaretest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import uk.co.scribbleapps.accesscaretest.R;
import uk.co.scribbleapps.accesscaretest.data.User;
import uk.co.scribbleapps.accesscaretest.util.ApplicationHelper;
import uk.co.scribbleapps.accesscaretest.util.PhotoHelper;

public class ProfileDetailsRequestFragment extends Fragment {

    private static final String TAG = "ProfileDetailsReqTAG";
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private Boolean photoTaken;
    private Button buttonTakePhoto;
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextSurname;
    private EditText editTextYearOfBirth;
    private PhotoHelper photoHelper;
    private final ApplicationHelper applicationHelper = ApplicationHelper.getInstance();

    public static ProfileDetailsRequestFragment newInstance() {
        return new ProfileDetailsRequestFragment();
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

    // Pop the Fragment backstack when the options menu back button is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Set the camera activity launcher - ready to listen for a result
    // In our menu, show the back button, and set the title
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Prevents two fragments being shown at once
        if (container != null) {
            container.removeAllViews();
        }

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Profile");

        // Register for the activity result before the view is created
        // Replaces the deprecated startActivityForResult()
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Toast.makeText(getContext(), getResources().getString(R.string.photo_taken_success), Toast.LENGTH_LONG).show();
                            photoTaken = true;
                            buttonTakePhoto.setText(getString(R.string.retake_photo));
                        }
                    }
                });

        return inflater.inflate(R.layout.profile_details_request_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoHelper = new PhotoHelper();
        photoTaken = false;

        // Get references to views
        editTextFirstName = view.findViewById(R.id.profileDetailsRequestFirstNameEditText);
        editTextSurname = view.findViewById(R.id.profileDetailsRequestSurnameEditText);
        editTextEmail = view.findViewById(R.id.profileDetailsRequestEmailEditText);
        editTextYearOfBirth = view.findViewById(R.id.profileDetailsRequestYearOfBirthTextView);
        buttonTakePhoto = view.findViewById(R.id.profileDetailsRequestCameraButton);
        Button buttonSave = view.findViewById(R.id.profileDetailsRequestSaveButton);

        // Launch the camera app
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
            }
        });

        // If all fields have been completed and we have a photo, save everything and open ProfileFragment
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAllFieldsCompleted()) {
                    String username = applicationHelper.getCurrentUsername();
                    String password = applicationHelper.getCurrentPassword();
                    User user = new User(
                            username,
                            password,
                            editTextFirstName.getText().toString().trim(),
                            editTextSurname.getText().toString().trim(),
                            editTextEmail.getText().toString().trim(),
                            editTextYearOfBirth.getText().toString().trim(),
                            photoHelper.getFileProvider(getContext(), username).toString().trim());
                    applicationHelper.replaceUser(user);
                    /*applicationHelper.saveStringToSharedPrefs("firstName", editTextFirstName.getText().toString());
                    applicationHelper.saveStringToSharedPrefs("surname", editTextSurname.getText().toString());
                    applicationHelper.saveStringToSharedPrefs("emailAddress", editTextEmail.getText().toString());
                    applicationHelper.saveStringToSharedPrefs("yearOfBirth", editTextYearOfBirth.getText().toString());*/
                    MainActivity.replaceFragment(ProfileFragment.newInstance(), false);
                } else {
                    Toast.makeText(getContext(), "Please complete all of the fields above", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Returns true if all fields contains at least one character
    private Boolean checkAllFieldsCompleted() {
        return photoTaken
                && editTextFirstName.getText().toString().length() > 0
                && editTextSurname.getText().toString().length() > 0
                && editTextEmail.getText().toString().length() > 0
                && editTextYearOfBirth.getText().toString().length() > 0;
    }

    // Use an Intent to take a picture and return control to the calling application
    private void onLaunchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoHelper.getFileProvider(getContext(), applicationHelper.getCurrentUsername()));
        cameraActivityResultLauncher.launch(intent);
    }
}