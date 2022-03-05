package com.example.myapplication.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class IamportCordova extends Activity {
    Intent intent;

    static final int REQUEST_CODE = 6018;
    static final int REQUEST_CODE_FOR_NICE_TRANS = 4117;
    static final String WEBVIEW_PATH = "file:///android_asset/iamport-webview.html";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iamport_payment_activity);

        Intent intent = new Intent(this.getIntent());
        String data = intent.getStringExtra("data");

        Log.d("get intent data:", data);

        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(WEBVIEW_PATH);
        webView.setWebChromeClient(new WebChromeClient());

//        webView.setWebViewClient(new WebViewActivity.WebViewClientClass());
    }

//    public CallbackContext callback;

    public boolean execute(String action, JSONArray args) throws JSONException {
        if (action.equals("startActivity")) {
//            callback = callbackContext;

            intent = new Intent(this.getApplicationContext(), IamportActivity.class);

            String type = args.getString(0);;
            JSONObject titleOptions = args.getJSONObject(1);;
            JSONObject params = args.getJSONObject(2);;
            intent.putExtra("type", type);
            intent.putExtra("titleOptions", titleOptions.toString());
            intent.putExtra("params", params.toString());

//            cordova.setActivityResultCallback(this);
            this.startActivityForResult(intent, REQUEST_CODE);

            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE && intent != null) {
            Bundle extras = intent.getExtras();
            String url = extras.getString("url");
//            callback.success(url);
        }
    }
}
