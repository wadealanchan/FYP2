package com.example.alan.fyp.model;

import com.example.alan.fyp.viewModel.ChatViewModel;

import java.text.SimpleDateFormat;

/**
 * Created by reale on 18/11/2016.
 */

public class ChatMessage {
    private String messageText;
    private String questioner;
    private String answerer;
    public  java.util.Date date;
    public  String postID;


    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getQuestioner() {
        return questioner;
    }

    public void setQuestioner(String questioner) {
        this.questioner = questioner;
    }

    public void setMessageDate(java.util.Date date) {
        this.date=date;
    }

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }



    public ChatViewModel toViewModel() {
        ChatViewModel c = new ChatViewModel();
        c.messagetext.set(this.messageText);
        c.setMessageUserId(this.answerer);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(this.date);
        c.timestamp.set(dateString);
        return c;
    }

}
