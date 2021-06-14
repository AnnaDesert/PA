package com.example.pa;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessViewHolder> {
    static ArrayList<ChatForPerson> chats;

    public MessageRecyclerViewAdapter(ArrayList<ChatForPerson> chats){
        this.chats = chats;
        //for(int i = 0; i < chats.size(); i++){
            //System.out.println("NUMBER" + (i+1) +chats.get(i).getMess());
        //}
    }
    //////////////////////////////////////////////////////////////
    class MessViewHolder extends RecyclerView.ViewHolder {
        TextView text_mess;

        public MessViewHolder(@NonNull View itemView) {
            super(itemView);
            text_mess = itemView.findViewById(R.id.text_mess);
        }

    }
    ////////////////////////////////////////////////////////////////////
    @NonNull
    @Override
    public MessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessViewHolder holder, int position) {
        Log.i("TAG REC",chats.get(position).getMess());
        holder.text_mess.setText(chats.get(position).getMess());//Добавление текста в кнопку
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
