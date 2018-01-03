package com.example.alan.fyp.model;

import android.support.annotation.NonNull;

import com.example.alan.fyp.viewModel.ChatViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wadealanchan on 2/1/2018.
 */

public class Con_Message implements Comparable {


    private String messageText;
    private String SenderID;
    private java.util.Date date;
    private String imageuri;


    public Con_Message() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Con_Message m = (Con_Message)o;
        return this.date.compareTo(m.getDate());
    }


//    public ChatViewModel toViewModel() {
//        ChatViewModel c = new ChatViewModel();
//        c.messagetext.set(this.messageText);
//        c.setMessageUserId(this.answerer);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = sdf.format(this.date);
//        c.timestamp.set(dateString);
//        return c;
//    }
}
