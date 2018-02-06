package com.example.alan.fyp.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wadealanchan on 2/1/2018.
 */

public class model_conversation {

    private String aid;
    private String postId;
    private List<Con_Message> messageList;
    private String postUserId;
    private boolean chatIsOver ;

    public model_conversation() {
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

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public boolean isChatIsOver() {
        return chatIsOver;
    }

    public void setChatIsOver(boolean chatIsOver) {
        this.chatIsOver = chatIsOver;
    }


}
