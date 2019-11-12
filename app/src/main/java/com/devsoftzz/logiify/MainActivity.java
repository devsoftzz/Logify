package com.devsoftzz.logiify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private ImageView mRefresh;
    private SharedPreferences mStorage;
    private LinearLayout mYES,mNO,mRotate;
    private String USERNAME,PASSWORD;
    private String URL = "https://10.100.56.55:8090/";
    private boolean second = false;
    private Dialog dialog,dialogWIFI;
    private TypeFaces typeFaces = new TypeFaces();
    private char[][] values = typeFaces.getData();
    private StringBuilder TempUSER = new StringBuilder();
    private WifiManager wifi;
    private Animation rotate;

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

        mYES = findViewById(R.id.yes);
        mNO = findViewById(R.id.no);
        mNO.setVisibility(View.VISIBLE);
        mYES.setVisibility(View.INVISIBLE);

        USERNAME = mStorage.getString("username","");
        PASSWORD = mStorage.getString("password","");


        mRotate = findViewById(R.id.rotation);

        loadSite();

        mRefresh = findViewById(R.id.refresh);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshView();
            }
        });

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifi.getWifiState()==WifiManager.WIFI_STATE_DISABLED){
            wifi.setWifiEnabled(true);
            showWIFIDialog();
        }

    }

    private void showWIFIDialog() {

        dialogWIFI = new Dialog(this);
        dialogWIFI.setContentView(R.layout.wifi_pop);
        dialogWIFI.setCanceledOnTouchOutside(true);
        TextView done = dialogWIFI.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSite();
                dialogWIFI.dismiss();}});
        dialogWIFI.show();
    }

    private void refreshView() {
        rotateButton();
        second=false;
        generateUSER();
        mWebView.loadUrl(URL);
    }

    private void loadSite() {
        rotateButton();
        generateUSER();
        configureWebView();
        mWebView.loadUrl(URL);
    }

    private void rotateButton() {
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.round);
        mRotate.startAnimation(rotate);
    }

    private void configureWebView() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String js = "javascript:" +
                        "document.getElementById('password').value = '" + PASSWORD + "';"  +
                        "document.getElementById('username').value = '" + TempUSER.toString() + "';";

                if(!second){
                    js += "document.getElementById('loginbutton').click()";
                    second=true;
                }
                view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        if(!s.equals("null")){
                            mYES.setVisibility(View.VISIBLE);
                            mNO.setVisibility(View.INVISIBLE);
                        }else {
                            mNO.setVisibility(View.VISIBLE);
                            mYES.setVisibility(View.INVISIBLE);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRotate.clearAnimation();
                            }
                        },1500);
                    }
                });
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                Toast.makeText(getApplicationContext(), "Something Want Wrong", Toast.LENGTH_LONG).show();
                mNO.setVisibility(View.VISIBLE);
                mYES.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void generateUSER() {

        TempUSER.delete(0,TempUSER.length());

        String RANDOM = String.valueOf(new Random().nextInt(900000000) + 100000000);
        for (int i=0;i<USERNAME.length();i++){
            randomChar(i,RANDOM.charAt(i));
        }
    }

    private void randomChar(int i,char rand) {

        int token;
        token = (int) rand%values.length;
        switch (USERNAME.charAt(i)){
            case '0':
                TempUSER.append(values[token][0]);
                break;
            case '1':
                TempUSER.append(values[token][1]);
                break;
            case '2':
                TempUSER.append(values[token][2]);
                break;
            case '3':
                TempUSER.append(values[token][3]);
                break;
            case '4':
                TempUSER.append(values[token][4]);
                break;
            case '5':
                TempUSER.append(values[token][5]);
                break;
            case '6':
                TempUSER.append(values[token][6]);
                break;
            case '7':
                TempUSER.append(values[token][7]);
                break;
            case '8':
                TempUSER.append(values[token][8]);
                break;
            case '9':
                TempUSER.append(values[token][9]);
                break;

            default:
                TempUSER.append(USERNAME.charAt(i));

        }

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
                showInfo();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void showInfo() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_info);
        dialog.setCanceledOnTouchOutside(true);
        TextView wp = dialog.findViewById(R.id.wp);
        TextView github = dialog.findViewById(R.id.code);
        wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectWhatsApp();}});
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webURL(); }});
        dialog.show();

    }

    private void webURL() {

        String url = "https://github.com/devsoftzz/Logify_";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        dialog.dismiss();

    }

    private void redirectWhatsApp() {
        if(isWPInstalled()){

            Uri mUri = Uri.parse("smsto:+919824978996");
            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
            mIntent.setPackage("com.whatsapp");
            mIntent.putExtra("chat",true);
            startActivity(mIntent);

        }else {

            Toast.makeText(getApplicationContext(),"WhatsApp isn't Installed.",Toast.LENGTH_LONG).show();

        }
        dialog.dismiss();
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
