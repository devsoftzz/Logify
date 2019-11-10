package com.devsoftzz.logify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private SharedPreferences mStorage;
    private String USERNAME,PASSWORD;
    private String URL = "https://10.100.56.55:8090/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        mWebView = findViewById(R.id.webview);
        mStorage = getSharedPreferences("logify",MODE_PRIVATE);

        if(mStorage.getBoolean("first",true)){
            startActivity(new Intent(MainActivity.this,Credentials.class));
            finish();
        }

        USERNAME = mStorage.getString("username","");
        PASSWORD = mStorage.getString("password","");

        loadSite();
    }

    private void loadSite() {

        generateUSER();

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.loadUrl(URL);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                final String js = "javascript:" +
                        "document.getElementById('password').value = '" + PASSWORD + "';"  +
                        "document.getElementById('username').value = '" + USERNAME + "';";

                view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
//                if (progress < 100) {
//                    progressDialog.show();
//                }
//                if (progress = = 100) {
//                    progressDialog.dismiss();
//                }
            }
        });

    }

    private void generateUSER() {
        TypeFaces typeFaces = new TypeFaces();
        char[][] values = typeFaces.getData();
        int token=0;
        String TempUSER = "";
        String RANDOM = String.valueOf(new Random().nextInt(900000000) + 100000000);
        //Toast.makeText(getApplicationContext(),String.valueOf(values.length),Toast.LENGTH_LONG).show();
        for (int i=0;i<USERNAME.length();i++){
            token = (int) RANDOM.charAt(i)%values.length;
            switch (USERNAME.charAt(i)){
                case '0':
                    TempUSER += values[token][0];
                    break;
                case '1':
                    TempUSER += values[token][1];
                    break;
                case '2':
                    TempUSER += values[token][2];
                    break;
                case '3':
                    TempUSER += values[token][3];
                    break;
                case '4':
                    TempUSER += values[token][4];
                    break;
                case '5':
                    TempUSER += values[token][5];
                    break;
                case '6':
                    TempUSER += values[token][6];
                    break;
                case '7':
                    TempUSER += values[token][7];
                    break;
                case '8':
                    TempUSER += values[token][8];
                    break;
                case '9':
                    TempUSER += values[token][9];
                    break;

                    default:
                        TempUSER += USERNAME.charAt(i);

            }
        }
        USERNAME = TempUSER;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.credentials:
                //Toast.makeText(getApplicationContext(),"Credentials",Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,Credentials.class));
                return true;
            case R.id.info:
                //Toast.makeText(getApplicationContext(),"Info",Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.activity_info);
                dialog.setCanceledOnTouchOutside(true);
                TextView wp = dialog.findViewById(R.id.wp);
                TextView github = dialog.findViewById(R.id.code);

                wp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isWPInstalled()){

                            Uri mUri = Uri.parse("smsto:+919824978996");
                            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                            mIntent.setPackage("com.whatsapp");
                            mIntent.putExtra("chat",true);
                            startActivity(mIntent);
                            dialog.dismiss();

                        }else {
                            Toast.makeText(getApplicationContext(),"WhatsApp isn't Installed.",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                github.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://www.example.com";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });

                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean isWPInstalled() {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
