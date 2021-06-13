package com.example.pa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.net.CookieManager;
import java.util.ArrayList;

import okhttp3.FormBody;

public class Chat extends AppCompatActivity {
    private Thread thread;
    private Runnable runnable;

    CookieManager cookieManager;
    String URL_Chat = "http://oreluniver.ru/chat";

    public static ArrayList<PersonForChat> ForChats = new ArrayList<>(1);
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Log.i("TAG","Чат открыт");
        cookieManager = MainActivity.cookieManager;
        init();

    }

    private void init(){
        runnable = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                String chat_str = ResponseConnect.conn(cookieManager,URL_Chat);
                parseBody(chat_str);
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void parseBody(String responseString){
        Document doc = Jsoup.parse(responseString);

        //Список преподователей
        Elements select = doc.select("#sender");// SELECT
        Elements option_select = select.get(0).getElementsByTag("option");// LIST OPTION

        for(Element option : option_select) {
            String namePerson = option.text(); // ФИО
            String idPerson = option.attr("value"); // ID
            ForChats.add(new PersonForChat(namePerson, idPerson));// Список преподователей
        }
        //Log.i("ForChats size", String.valueOf(ForChats.size()));
        //for(int i = 0; i < ForChats.size();i++){ Log.i("ForChats size", ForChats.get(i).Name);}

        //Чаты


        // JSOUP
        Elements input_select = doc.select("#input > div");

        for(Element div : input_select){
            Elements time = div.select("div.col-md-9 > p.text-muted");//Time
            Elements message = div.select("div.col-md-9 > p:nth-child(3)");// TEXT
            Elements idthisMess = div.select("div.col-md-1 > a:nth-child(1)"); //ID

            String timeMess = time.text(); // Time
            String messMess = message.text(); //Text
            String idMess = idthisMess.attr("onclick").replaceAll("\\D+",""); // ID

            Log.i("TAG", timeMess);
            Log.i("TAG", messMess);
            Log.i("TAG", idMess);
        }
    }

}