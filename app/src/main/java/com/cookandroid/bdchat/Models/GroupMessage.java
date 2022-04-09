package com.cookandroid.bdchat.Models;

public class GroupMessage {
    private String recName, recMsg, recDate, recTime, senMsg, senDate, senTime;
    public GroupMessage(String recName, String recMsg, String recDate, String recTime, String senMsg, String senDate, String senTime ) {
        this.recName=recName;
        this.recMsg=recMsg;
        this.recDate=recDate;
        this.recTime=recTime;

        this.senMsg=senMsg;
        this.senDate=senDate;
        this.senTime=senTime;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecMsg() {
        return recMsg;
    }

    public void setRecMsg(String recMsg) {
        this.recMsg = recMsg;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public String getSenMsg() {
        return senMsg;
    }

    public void setSenMsg(String senMsg) {
        this.senMsg = senMsg;
    }

    public String getSenDate() {
        return senDate;
    }

    public void setSenDate(String senDate) {
        this.senDate = senDate;
    }

    public String getSenTime() {
        return senTime;
    }

    public void setSenTime(String senTime) {
        this.senTime = senTime;
    }
}
