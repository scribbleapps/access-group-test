package uk.co.scribbleapps.accesscaretest.ui;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created to ensure the website is opened in-app, otherwise an Intent is used to open
 * the website in the user's web browser of choice.
 */

public class CustomWebViewClient extends WebViewClient {

    private Activity activity;

    public CustomWebViewClient(Activity activity) {
        this.activity = activity;
    }

    // Ensures the website opens in-app
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

}
