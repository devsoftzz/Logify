package com.devsoftzz.logiify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Credentials extends AppCompatActivity {

    private TextInputLayout mUser,mPass;
    private SharedPreferences mStorage;
    private String USER,PASS;
    private Button mEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        getSupportActionBar().setElevation(0);

        mUser = findViewById(R.id.username);
        mPass = findViewById(R.id.password);
        mStorage = getSharedPreferences("logify",MODE_PRIVATE);
        mEnter = findViewById(R.id.set);

        USER = mStorage.getString("username","");
        PASS = mStorage.getString("password","");

        mUser.getEditText().setText(USER);
        mPass.getEditText().setText(PASS);

        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userValue = mUser.getEditText().getText().toString().trim();
                String passValue = mPass.getEditText().getText().toString().trim();

                if(userValue.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Username",Toast.LENGTH_LONG).show();
                    return;
                }else if(passValue.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    SharedPreferences.Editor editor = mStorage.edit();
                    editor.putString("username",userValue);
                    editor.putString("password",passValue);
                    editor.putBoolean("first",false);
                    editor.commit();
                    startActivity(new Intent(Credentials.this,MainActivity.class));
                    finish();
                }
            }
        });
    }
}
