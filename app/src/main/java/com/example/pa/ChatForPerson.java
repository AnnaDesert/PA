package com.example.pa;

public class ChatForPerson {
    String Time;
    String Text;
    String adresant;
    String id;
    String Name;

    public ChatForPerson(String Time, String Name, String Text, String id, String adresant){
        this.Time = Time;
        this.Name = Name;
        this.Text = Text;
        this.adresant = adresant;
        this.id = id;
    }
}
