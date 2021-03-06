package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class IamportWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) { // 컨펌 창 뜨려고 할때
        Log.d("message:",message);
        Log.d("url:",url);

        new AlertDialog.Builder(view.getContext())
                .setTitle(url + "에 삽입된 내용") // 컨펌 타이틀
                .setMessage(message) // 컨펌 메시지
                .setPositiveButton( // 확인버튼 눌렀을때
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("확인:",message);
                                result.confirm();
                            }
                        }
                )
                .setNegativeButton( // 취소버튼 눌렀을때
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("취소:",message);
                                result.cancel();
                            }
                        }
                )
                .create()
                .show();

        return true;
    }
}