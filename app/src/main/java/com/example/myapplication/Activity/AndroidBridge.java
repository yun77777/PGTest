package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidBridge {

    WebView webView;
    Activity activity;

    public AndroidBridge(WebView webView, Activity activity) {
        this.webView = webView;
        this.activity = activity;
    }

    @JavascriptInterface
    public void test(String str, String type){
//        final public Handler handler = new Handler();
//        Intent intent = new Intent(activity, IamportCordova.class);
        Intent intent = new Intent(activity, IamportActivity.class);

        Log.d("str:",str);
        JSONObject json = null;
        try {
            json = new JSONObject(str);
            Log.d("titleOptions:", String.valueOf(json.get("titleOptions")));
            String titleOptions = String.valueOf(json.get("titleOptions"));
            String userCode = String.valueOf(json.get("userCode"));
            String data = String.valueOf(json.get("data"));
//            String callback = String.valueOf(json.get("callback"));
            intent.putExtra("params", str);
            intent.putExtra("titleOptions", titleOptions);
            intent.putExtra("userCode", userCode);
            intent.putExtra("data", data);
            intent.putExtra("type", type);


//            intent.putExtra("callback", callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject finalJson = json;
        Log.d("json:", String.valueOf(finalJson));


        intent.putExtra("data", str);
        activity.startActivity(intent);

        Log.d("intent:",intent.toString());
//        webView.post(new Runnable() {
//            @Override
//            public void run() {
//                webView.loadUrl("javascript:alert('"+ finalJson +"')");
//            }
//        });

    }


}

