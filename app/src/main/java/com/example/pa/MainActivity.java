package com.example.pa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

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
    String URL_personal = "http://oreluniver.ru/student/personal";

    String res = null;

    class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {

Log.i("STEP","Create Manager and Client");

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build();

Log.i("STEP","Create Form");

            RequestBody form = new FormBody.Builder()
                    .add("studentBookId", "170387")
                    .addEncoded("f","%CB%F3%EA%FC%FF%ED%EE%E2%E0")
                    .addEncoded("i","%C0%ED%ED%E0")
                    .addEncoded("pass","yehAFFQDfZ")
                    .add("class","pwd")
                    .add("submit","%C2%EE%E9%F2%E8")
                    .build();

Log.i("STEP","Start Request");

            Request request = new Request.Builder()
                    .url(URL_student)
                    .post(form)
                    .build();
            Call call = client.newCall(request);

Log.i("STEP","Start Response");

            Response response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //CookieStore cookieStore = cookieManager.getCookieStore();
            //String cookieString = String.valueOf(cookieStore.getCookies());

//Log.i("Response Cookie", String.valueOf(cookieStore.getCookies()));
Log.i("Response Status Code", String.valueOf(response.code()));
Log.i("Response Message",response.message());
/// Запрос  с куки
            if( response.code() == 302){
Log.i("Connect Person","Start");

                OkHttpClient client_person = new OkHttpClient().newBuilder()
                        .cookieJar(new JavaNetCookieJar(cookieManager))
                        .followRedirects(false)
                        .followSslRedirects(false)
                        .build();

                Request request_person = new Request.Builder()
                        .url(URL_personal)
                        .build();

                try {
                    Response response_person = client_person.newCall(request_person).execute();

                    String responseString1 = response_person.body().string();
                    int responseCode3 = response_person.code();

                    response_person.body().close();

                    System.out.println("Response code : " + responseCode3);
                    System.out.println("Response code : " + responseString1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

Log.i("STEP","Finally");

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                String parameters = "studentBookId=170387&f=%CB%F3%EA%FC%FF%ED%EE%E2%E0&i=%C0%ED%ED%E0&pass=yehAFFQDfZ&class=pwd&submit=%C2%EE%E9%F2%E8";// Redact!!!

                res = String.valueOf(new AsyncRequest().execute(parameters));
            }
        });
    }

}