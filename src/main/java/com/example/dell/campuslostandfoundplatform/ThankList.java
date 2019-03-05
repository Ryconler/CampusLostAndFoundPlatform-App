package com.example.dell.campuslostandfoundplatform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThankList {
    private String sUsername;
    private String rUsername;
    private String content;
    private String date;

    public String getsUsername() {
        return sUsername;
    }

    public void setsUsername(String sUsername) {
        this.sUsername = sUsername;
    }

    public String getrUsername() {
        return rUsername;
    }

    public void setrUsername(String rUsername) {
        this.rUsername = rUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ThankList(String sUsername, String rUsername, String content,String date) {
        this.sUsername = sUsername;
        this.rUsername = rUsername;
        this.content = content;
        this.date=date;
    }
    public String getDifTime() {
        String difTime="刚刚";
        long difMin=0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowdate = df.format(new Date());
            Date d1 = df.parse(nowdate);
            Date d2 = df.parse(getDate());
            difMin=(d1.getTime() - d2.getTime())/(1000*60);
            if (difMin<60&&difMin>0)difTime=String.valueOf(difMin)+"分钟前";
            if (difMin>=60&&difMin<=1440)difTime=String.valueOf(difMin/60)+"小时前";
            if (difMin>1440)difTime=String.valueOf(difMin/1440)+"天前";
        }catch (Exception e){
            e.printStackTrace();
        }
        return difTime;
    }
}
