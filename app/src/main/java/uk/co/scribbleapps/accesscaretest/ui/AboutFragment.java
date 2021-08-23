package uk.co.scribbleapps.accesscaretest.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import uk.co.scribbleapps.accesscaretest.data.User;
import uk.co.scribbleapps.accesscaretest.util.ApplicationHelper;
import uk.co.scribbleapps.accesscaretest.R;

public class AboutFragment extends Fragment {

    private ApplicationHelper applicationHelper = ApplicationHelper.getInstance();
    private static final String TAG = "AboutFragmentTAG";

    public static AboutFragment newInstance() {
        return new AboutFragment();
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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("About");

        return inflater.inflate(R.layout.about_fragment, container, false);
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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = applicationHelper.getUser(applicationHelper.getCurrentUsername());

        // Show the first name and surname together
        TextView textViewName = view.findViewById(R.id.aboutNameTextView);
        textViewName.setText(String.format("%s %s", user.getFirstName(), user.getSurname()));

        // Show the email address - open email app chooser when selected
        TextView textViewEmail = view.findViewById(R.id.aboutEmailTextView);
        String emailAddress = user.getEmailAddress();
        textViewEmail.setPaintFlags(textViewEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewEmail.setText(emailAddress);
        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);
                sendEmail.setType("plain/text");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailAddress});
                //sendEmail.setData(Uri.parse(emailAddress));
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, "Access Care Test Enquiry");
                startActivity(Intent.createChooser(sendEmail, "Send email..."));
            }
        });
    }

}