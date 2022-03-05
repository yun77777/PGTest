package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.Activity.IamportActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import javax.security.auth.callback.Callback;

/**
 * This class echoes a string called from JavaScript.
 */
public class IamportCordova extends Activity {
//    public class IamportCordova extends CordovaPlugin {
    Intent intent;

    static final int REQUEST_CODE = 6018;
    static final int REQUEST_CODE_FOR_NICE_TRANS = 4117;
    static final String WEBVIEW_PATH = "file:///android_asset/PLUGINS/iamport/iamport-webview.html";
//    public CallbackContext callback;

    public boolean execute(String action, JSONArray args) throws JSONException {
        if (action.equals("startActivity")) {

            intent = new Intent(this.getApplicationContext(), IamportActivity.class);

            String type = args.getString(0);
            JSONObject titleOptions = args.getJSONObject(1);
            JSONObject params = args.getJSONObject(2);
            intent.putExtra("type", type);
            intent.putExtra("titleOptions", titleOptions.toString());
            intent.putExtra("params", params.toString());

//            this.setActivityResultCallback(this);
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