package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.net.URISyntaxException;

abstract public class IamportWebViewClient extends WebViewClient {
    Activity activity;
    WebView webView;

    protected String userCode;
    protected JSONObject data;
    protected String triggerCallback;
    private String redirectUrl;

    protected Boolean loadingFinished = false;

    protected final static String MARKET_PREFIX = "market://details?id=";

    public IamportWebViewClient(Activity activity, String params) {
        this.activity = activity;

        try {
            JSONObject jsonParams = new JSONObject(params);
            Log.d("check jsonParams:", String.valueOf(jsonParams));

            userCode = jsonParams.getString("userCode");
            data = jsonParams.getJSONObject("data");
            triggerCallback = "function(e){console.log('e:',e);}";
//            triggerCallback = jsonParams.getString("triggerCallback");
            redirectUrl = jsonParams.getString("redirectUrl");

            Log.d("check redirectUrl:",redirectUrl);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("url", url);
        Log.d("check@ redirectUrl:",redirectUrl);

        this.webView = view;

        if (isOver(url)) {
            Log.d("check@ 1:",redirectUrl);

            Intent data = new Intent();
            data.putExtra("url", url);

            activity.setResult(IamportCordova.REQUEST_CODE, data);
            activity.finish();

            return true;
        }

        Log.d("check@ redirectUrl:",redirectUrl);
        Log.d("check@ url:",url);
        Log.d("check@ 2?:",String.valueOf(isUrlStartsWithProtocol(url)));

        if (isUrlStartsWithProtocol(url)) return false;
        Log.d("url@ 3:",url);

        Intent intent = null;
        try {
            if (isNiceTransOver(url)) {
                startActivityForTrans(url);
                return true;
            }

            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); // Intent URI 처리
            if (intent == null) return false;

            startNewActivity(intent.getDataString());
            return true;
        } catch(URISyntaxException e) {
            return false;
        } catch(ActivityNotFoundException e) { // PG사에서 호출하는 url에 package 정보가 없는 경우
            String scheme = intent.getScheme();
            if (isSchemeNotFound(scheme)) return true;

            String packageName = intent.getPackage();
            if (packageName == null) return false;

            startNewActivity(MARKET_PREFIX + packageName);
            return true;
        }
    }

    /* WebView가 load되면 IMP.init, IMP.request_pay를 호출한다 */
    abstract public void onPageFinished(WebView view, String url);

    /* url이 https, http 또는 javascript로 시작하는지 체크 */
    protected boolean isUrlStartsWithProtocol(String url) {
        return url.startsWith("https://") || url.startsWith("http://") || url.startsWith("javascript:");
    }

    /* 결제/본인인증 종료되었는지 여부를 판단한다 */
    protected boolean isOver(String url) {
        Log.d("redirectUrl:",redirectUrl);
        return url.startsWith(redirectUrl);
    }

    protected boolean isNiceTransOver(String url) {
        return false;
    }

    protected void startActivityForTrans(String url) {}

    protected void startNewActivity(String parsingUri) {
        Uri uri = Uri.parse(parsingUri);
        Intent newIntent = new Intent(Intent.ACTION_VIEW, uri);

        activity.startActivity(newIntent);
    }

    /* ActivityNotFoundException에서 market 실행여부 확인 */
    protected boolean isSchemeNotFound(String scheme) {
        return false;
    }

    /* 나이스 - 실시간 계좌이체 인증 후 후속처리 루틴 */
    protected void bankPayPostProcess(int requestCode, int resultCode, Intent data) {}
}