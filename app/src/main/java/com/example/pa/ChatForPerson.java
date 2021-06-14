package com.example.pa;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatForPerson implements Comparable<ChatForPerson>{
    long Time_Unix;
    String Time;
    String Text;
    String adresant;
    String id;
    String Name;

    public ChatForPerson(String Time, String Name, String Text, String id, String adresant){
        this.Name = Name;
        this.Text = Text;
        this.adresant = adresant;
        this.id = id;
        this.Time = Time;

        Date timer = null;
        try {
            timer = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timer);
        this.Time_Unix = calendar.getTimeInMillis()/10000;

    }
    public String getMess(){
        String mess = this.Time + "\n" + Name + "\n" + this.Text;
        return mess;
    }

    @Override
    public int compareTo(ChatForPerson person) {
        long result = person.Time_Unix - this.Time_Unix;
        return (int) result;
    }
}
