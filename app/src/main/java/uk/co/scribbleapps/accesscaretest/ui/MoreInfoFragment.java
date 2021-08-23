package uk.co.scribbleapps.accesscaretest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import uk.co.scribbleapps.accesscaretest.R;

public class MoreInfoFragment extends Fragment {

    private WebView webView;
    private static final String TAG = "MoreInfoFragmentTAG";

    public static MoreInfoFragment newInstance() {
        return new MoreInfoFragment();
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

    // Set the refresh icon to visible
    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_refresh).setVisible(true);
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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("More Info");

        return inflater.inflate(R.layout.more_info_fragment, container, false);
    }

    // Pop the Fragment backstack when the option menu back button is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
            return true;
        }
        if (item.getItemId() == R.id.action_refresh) {
            webView.reload();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the WebView to show the Access Group website
        webView = view.findViewById(R.id.moreInfoWebView);
        WebSettings webSettings = webView.getSettings();
        // JavaScript needed to select OK to Cookies
        webSettings.setJavaScriptEnabled(true);
        // Custom WebViewClient needed to ensure the page opens in-app
        CustomWebViewClient webViewClient = new CustomWebViewClient(getActivity());
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://www.theaccessgroup.com");
    }

}