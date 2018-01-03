package com.example.alan.fyp.model;

import com.example.alan.fyp.viewModel.ChatViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wadealanchan on 2/1/2018.
 */

public class Conversation {

    private String aid;
    private String postId;
    private List<Con_Message> messageList;

    public Conversation() {
        messageList = new ArrayList<Con_Message>();
    }

    public String getAid(){
        return aid;
    }

    public void setAid(String aid){
       this.aid=aid;
    }

    public String getPostId(){
        return postId;
    }

    public void setPostId(String postId){
        this.postId=postId;
    }

    public List<Con_Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Con_Message> messageList) {
        this.messageList = messageList;
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
