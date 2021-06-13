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
            ForChats.add(new PersonForChat(namePerson, idPerson, null, null, 0));// Список преподователей
        }

        //Чаты
        ArrayList<ArrayList<PersonForChat>> Chats = new ArrayList<ArrayList<PersonForChat>>(ForChats.size());
        ArrayList<PersonForChat> chat = new ArrayList<PersonForChat>(1);
        for(int i = 0; i < Chats.size();i++){
            chat.add(ForChats.get(i));
            Chats.add(chat);
        }

        //Elements desc_h3 = doc.select("#input > div:nth-child(1)");
        Elements time = doc.select("#input > div:nth-child(1) > div > div.col-md-9 > p.text-muted");// TIME
        Elements message = doc.select("#input > div:nth-child(1) > div > div.col-md-9 > p:nth-child(3)");// TEXT
        Elements idthisMess = doc.select("#input > div:nth-child(1) > div > div.col-md-1 > a:nth-child(1)"); //replyemp(ID)


       // Log.i("TAG", String.valueOf(Chats.get(0).size()));
    }

}