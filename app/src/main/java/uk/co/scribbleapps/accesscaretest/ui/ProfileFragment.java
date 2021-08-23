package uk.co.scribbleapps.accesscaretest.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import uk.co.scribbleapps.accesscaretest.R;
import uk.co.scribbleapps.accesscaretest.data.User;
import uk.co.scribbleapps.accesscaretest.util.ApplicationHelper;
import uk.co.scribbleapps.accesscaretest.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private final ApplicationHelper applicationHelper = ApplicationHelper.getInstance();
    private static final String TAG = "ProfileFragmentTAG";
    private ProfileViewModel viewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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

        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    // Pop the Fragment backstack when the option back button is selected
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

    // Get our references to the Layout Views, set the name, and show the photo
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Set the first and surname to textViewName
        TextView textViewName = view.findViewById(R.id.profileNameTextView);
        User user = applicationHelper.getUser(applicationHelper.getCurrentUsername());
        textViewName.setText(String.format("%s %s", user.getFirstName(), user.getSurname()));

        ImageView imageViewProfilePicture = view.findViewById(R.id.profileImageViewUserPhoto);
        Bitmap takenImage = viewModel.getImage(user.getUsername());
        imageViewProfilePicture.setImageBitmap(takenImage);

        // MoreInfoFragment shows the Access Group website
        Button buttonMoreInfo = view.findViewById(R.id.profileButtonMoreInfo);
        buttonMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.replaceFragment(MoreInfoFragment.newInstance(), true);
            }
        });

        // AboutFragment shows the user's full name and email address
        Button buttonAbout = view.findViewById(R.id.profileButtonAbout);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.replaceFragment(AboutFragment.newInstance(), true);
            }
        });
    }

}