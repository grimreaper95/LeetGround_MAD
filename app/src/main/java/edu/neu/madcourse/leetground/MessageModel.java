package com.example.demo_chat_app;

import java.text.DateFormat;
import java.util.Date;

public class MessageModel {

    public int id;
    public String message;
    public int messageType;
    public String username;
    public String messageTime;
    // Constructor
    public MessageModel(String message, int messageType, String username) {
        this.message = message;
        this.messageType = messageType;
        this.username =username;
        Date currentDate=new Date();
        this.messageTime= DateFormat.getTimeInstance(DateFormat.SHORT).format(currentDate);
    }
    public MessageModel(){

    }

    public String getMessage() {
        return message;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getUsername() {
        return username;
    }

    public String getMessageTime() {
        return messageTime;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "message='" + message + '\'' +
                ", messageType=" + messageType +
                ", username='" + username + '\'' +
                ", messageTime=" + messageTime +
                '}';
    }
}
