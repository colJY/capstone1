package com.cookandroid.bdchat.Models;
//메시지 모델 생성
public class MessageModel {
    //유저 아이디, 메시지, 메시지아이디, 타임스템프
    String uId, name, message, messageId;
    Long timestamp;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageModel(String uId, String name, String message, Long timestamp) {
        this.uId = uId;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessageModel(){

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
