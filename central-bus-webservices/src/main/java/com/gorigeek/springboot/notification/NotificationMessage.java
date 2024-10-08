package com.gorigeek.springboot.notification;

import java.util.Map;

public class NotificationMessage {
    
    private String recippientToken;
    private String tittle;
    private String body;
    private String imgNoti;
    
    public String getRecippientToken() {
        return recippientToken;
    }
    public void setRecippientToken(String recippientToken) {
        this.recippientToken = recippientToken;
    }
    public String getTittle() {
        return tittle;
    }
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getImgNoti() {
        return imgNoti;
    }
    public void setImgNoti(String imgNoti) {
        this.imgNoti = imgNoti;
    }

    
}
