package com.example.pa;

import java.util.ArrayList;

public class PersonForChat { //Список препод
    String Name;
    String id;
    ArrayList<ChatForPerson> chats = new ArrayList<ChatForPerson>(1);
    public PersonForChat(String Name, String id){ //с кем чат
        this.Name = Name;
        this.id = id;
    }
}
