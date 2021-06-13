package com.example.pa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    public Button Sсhedule;

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
        Sсhedule = findViewById(R.id.shedule);

        //CHAT
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG","ЧАТ!");
                Intent intent = new Intent(PersonalInfo.this, Chat.class);
                MainActivity.GGManager.getContext().startActivity(intent);
            }
        });
        //SCHEDULE
        Sсhedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        init();

        do {
            try {
                thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            textBody.setText(Html.fromHtml(Body));
        }while (Body.equals("Низкая скорость интернета, повторите соединение"));

    }

    private void init(){
        runnable = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                resBody = ResponseConnect.conn(cookieManager,URL_personal);
                Body = parseBody(resBody);// body
                //System.out.println("Body: " + Body);
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String parseBody(String responseString){
        Document doc = Jsoup.parse(responseString);

        Elements desc_h2 = doc.getElementsByTag("h2");
        Elements desc_nexth2 = desc_h2.nextAll();

        return desc_nexth2.get(0).html();
    }

}

