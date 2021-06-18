package com.example.pa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Collections;


public class Chat extends AppCompatActivity {
    private Thread thread;
    private Runnable runnable;

    static RecyclerView MessList;
    int flag;

    CookieManager cookieManager;
    String URL_Chat = "http://oreluniver.ru/chat";
    public static String pars_select_in = "#input > div";
    public static String pars_select_out = "#output > div";

    public static ArrayList<PersonForChat> ForChats = new ArrayList<>(1);
    public static ArrayList<ChatForPerson> Chats = new ArrayList<>(1);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MessList = findViewById(R.id.chats_list);// Нахождение RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Отображение Вью
        MessList.setLayoutManager(layoutManager); // Подключение Менеджера
        MessList.setHasFixedSize(true);// Добавление фиксированной длинны


        Log.i("TAG", "Чат открыт");
        cookieManager = MainActivity.cookieManager;
        init();

    }

    private void init() {
        flag = 0;
        runnable = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                String chat_str = ResponseConnect.conn(cookieManager, URL_Chat);
                parseBody(chat_str);
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void parseBody(String responseString) {
        Document doc = Jsoup.parse(responseString);

        //Список преподователей
        Elements select = doc.select("#sender");// SELECT
        Elements option_select = select.get(0).getElementsByTag("option");// LIST OPTION

        for (Element option : option_select) {
            String namePerson = option.text(); // ФИО
            String idPerson = option.attr("value"); // ID
            ForChats.add(new PersonForChat(namePerson, idPerson));// Список преподователей
        }
        flag = 1;
        //Log.i("ForChats size", String.valueOf(ForChats.size()));
        for (int i = 0; i < ForChats.size(); i++) { //System.out.print(ForChats.get(i).Name + " " + ForChats.get(i).id + "\n");
        }
        //Чаты
        // Входящие
        parsMess(doc, pars_select_in, "in");
        // Исходящие
        parsMess(doc, pars_select_out, "out");

        Collections.sort(Chats);
        // ДОБАВИТЬ СОРТИРОВКУ ПО ДАТАМ

        for (int i = 0; i < Chats.size(); i++) {
            //System.out.println(Chats.get(i).Time + " " + Chats.get(i).Name + " " + Chats.get(i).Text + " " + Chats.get(i).id);
        }
        // RECYCLER VIEW

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(Chats);
                MessList.setAdapter(messageRecyclerViewAdapter);

            }
        });


    }

    public static void parsMess(Document doc, String select, String adresant) {
        Elements input_select = doc.select(select);
        for (Element div : input_select) {
            Elements time = div.select("div.col-md-9 > p.text-muted");//Time
            Elements name = div.select(" div.col-md-9 > p:nth-child(2)"); // Name
            Elements message = div.select("div.col-md-9 > p:nth-child(3)");// TEXT
            Elements idthisMess = div.select("div.col-md-1 > a:nth-child(1)"); //ID

            String timeMess = time.text(); // Time
            String NameMess = name.text(); // Name
            String messMess = message.text(); //Text
            String idMess = idthisMess.attr("onclick").replaceAll("\\D+", ""); // ID

            if (!idMess.equals("")) {
                Chats.add(new ChatForPerson(timeMess, NameMess, messMess, idMess, adresant));
            }

            //Log.i("TAG", timeMess);
            //Log.i("TAG", messMess);
            //Log.i("TAG", idMess);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(flag == 1)
        switch (item.getItemId()) {
            case R.id.item1:
                Log.i("MENU","Start");
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                View view = getLayoutInflater().inflate(R.layout.new_mess, null);
                builder.setTitle("Отправить сообщение");

                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                ArrayAdapter<PersonForChat> adapter = new ArrayAdapter<PersonForChat>(Chat.this,
                        android.R.layout.simple_spinner_item, ForChats);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
        }
        return false;
    }
}