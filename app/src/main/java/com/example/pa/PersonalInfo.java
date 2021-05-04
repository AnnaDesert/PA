package com.example.pa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;

public class PersonalInfo extends AppCompatActivity {

    public TextView textBody;
    static String resBody = null;// строка общего пользования
    static String Body = "Нет текста";
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

        init();
        while (Body.equals("Нет текста")) {
            try {
                thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("SetText and text:" + Body);
        textBody.setText(Body);
    }

    private void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                resBody = conn(cookieManager,URL_personal); //html doc
                //System.out.println("ResBody: " + resBody);
                Body = parseBody(resBody);// body
                flag++; //&&&&&&&&&&&&&&&&&
                System.out.println("Body: " + Body);
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

    public static String parseBody(String responseString){
        Document doc = Jsoup.parse(responseString, "windows-1251");
        System.out.print("ResBode in function " + ":" + responseString + "\n");
        Elements desc_h2 = doc.getElementsByTag("h2");
        System.out.print("H2 " + ":" + desc_h2 + "\n");
        Elements desc_nexth2 = desc_h2.nextAll();
        System.out.print("Next H2 " + ":" + desc_nexth2 + "\n");
        Elements desc_div_fifst = desc_nexth2.get(0).getElementsByTag("div");
            System.out.print("Desc " + ":" + desc_div_fifst.get(0) + "\n");
        /*try {
            System.out.print("Desc utf-8" + ":" + URLEncoder.encode(desc_div_fifst.toString(),"UTF-8") + "\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return desc_div_fifst.get(0).text();
    }

}

