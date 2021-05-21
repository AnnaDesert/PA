package com.example.pa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;

public class PersonalInfo extends AppCompatActivity {

    public TextView textBody;
    public Button Chat;
    public Button Shedule;

    static String resBody = null;// строка общего пользования
    static String Body = "Низкая скорость интернета, повторите соединение";
    static int flag = 0;

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
        Chat = findViewById(R.id.chat);
        Shedule = findViewById(R.id.shedule);

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Shedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        init();
        try {
            thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textBody.setText(Html.fromHtml(Body));
    }

    private void init(){
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                resBody = conn(cookieManager,URL_personal); //html doc
                Body = parseBody(resBody);// body
                flag++; //&&&&&&&&&&&&&&&&&
                System.out.println("Body: " + Body);
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String conn(CookieManager cookieManager_two, String URL_two){//СМОТРИ СЮДА

        OkHttpClient client_person = new OkHttpClient().newBuilder()
                .cookieJar(new JavaNetCookieJar(cookieManager_two))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        Request request_person = new Request.Builder()
                .url(URL_two)
                .addHeader("Content-Type", "text/plain; charset=UTF-8")
                .build();

        String responseString1 = null;
        try {
            Response response_person = client_person.newCall(request_person).execute();

            byte[] responseBytes = Objects.requireNonNull(response_person.body()).bytes();
            responseString1 = new String(responseBytes, "windows-1251");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString1;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String parseBody(String responseString){
        Document doc = Jsoup.parse(responseString);

        Elements desc_h2 = doc.getElementsByTag("h2");
        Elements desc_nexth2 = desc_h2.nextAll();

        return desc_nexth2.get(0).html();
    }

}

