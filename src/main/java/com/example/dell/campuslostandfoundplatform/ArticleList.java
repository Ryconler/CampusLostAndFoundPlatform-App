package com.example.dell.campuslostandfoundplatform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleList {
    private String aid;
    private String title;
    private String date;
    private String state;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArticleList(String aid, String title, String date, String state){
        this.aid=aid;
        this.title=title;

        this.date=date;
        this.state=state;

    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
