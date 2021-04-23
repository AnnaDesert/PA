package com.example.pa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;

public class PersonalInfo extends AppCompatActivity {

    TextView textBody; // строка общего пользования
    static String resBody = null;

    CookieManager cookieManager = new CookieManager();
    String URL_personal = "http://oreluniver.ru/student/personal";


    private Thread thread;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        cookieManager = MainActivity.cookieManager;
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        textBody = findViewById(R.id.text_body);

        init();
    }

    private void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                resBody = conn(cookieManager,URL_personal);
                parseBody(resBody);
                System.out.println("ResBody: " + resBody);
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    public static String conn(CookieManager cookieManager_two, String URL_two){//СМОТРИ СЮДА

        OkHttpClient client_person = new OkHttpClient().newBuilder()
                .cookieJar(new JavaNetCookieJar(cookieManager_two))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        Request request_person = new Request.Builder()
                .url(URL_two)
                .build();

        String responseString1 = null;
        try {
            Response response_person = client_person.newCall(request_person).execute();

            responseString1 = response_person.body().string();
            response_person.body().close();
            //System.out.println("Res: " + responseString1);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString1;
    }

    public static void parseBody(String responseString){
        Document doc = Jsoup.parse(responseString);
    }
}

