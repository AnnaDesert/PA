package com.example.pa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;


public class MainActivity extends AppCompatActivity {
    EditText number, Fam, Name, pass;
    Button Enter;

    String URL_student = "http://oreluniver.ru/student";
    String fam;

    private Thread thread;
    private Runnable runnable;

    static CookieManager cookieManager = new CookieManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GGManager.setContext(this);
/*
        number = findViewById(R.id.textNumber);
        Fam = findViewById(R.id.textFam);
        Name = findViewById(R.id.textName);
        pass = findViewById(R.id.textPass);*/
        Enter = findViewById(R.id.enter);

        Enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*String  mNumber = number.getText().toString();
                String  mUserfam = Fam.getText().toString();
                String  mUsername = Name.getText().toString();
                String  mPass = pass.getText().toString();*/

                init();

            }
        });
    }

    private void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                connect();
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    private void connect(){
        Log.i("connect()","start");
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        RequestBody form = null;
        try {
            form = new FormBody.Builder()
                    .add("studentBookId", "170387")
                    .addEncoded("f", URLEncoder.encode("Лукьянова","windows-1251"))
                    .addEncoded("i", URLEncoder.encode("Анна","windows-1251"))
                    .addEncoded("pass","yehAFFQDfZ")
                    .add("class","pwd")
                    .add("submit", URLEncoder.encode("Войти","windows-1251"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(URL_student)
                .post(form)
                .build();
        Call call = client.newCall(request);


        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Response Status Code", String.valueOf(response.code()));

        if( response.code() == 302){
            Log.i("MainActivity","Finish");
            Intent intent = new Intent(GGManager.getContext(), PersonalInfo.class);
            GGManager.getContext().startActivity(intent);
        }

    }

}

class GGManager {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        GGManager.context = context;
    }

}