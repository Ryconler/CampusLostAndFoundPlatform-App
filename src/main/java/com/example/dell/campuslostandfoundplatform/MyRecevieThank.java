package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyRecevieThank extends AppCompatActivity {
    private static ListView listView;
    private static ThankListAdapter thankListAdapter;
    private static Handler handler=new Handler();
    private static String rUid;
    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recevie_thank);
        TextView text_title=(TextView)findViewById(R.id.text_title);
        text_title.setText("我收到的感谢信");
        listView=(ListView)findViewById(R.id.thankList);
        loadSharedPreferences();
        setListView();

    }
    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        rUid=String.valueOf(sharedPreferences.getInt("uid",0));
    }
    private void setListView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ThankList> thankList= new ArrayList<ThankList>();
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/listThankByRUid.action";

                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("rUid",rUid);
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        ThankList list =new ThankList(jsonObject.getJSONObject("userBySuid").getString("username")
                                ,jsonObject.getJSONObject("userByRuid").getString("username")
                                ,jsonObject.getString("content"),jsonObject.getString("date"));
                        thankList.add(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try
                    {
                        Thread.currentThread().sleep(100);//毫秒
                        thankListAdapter=new ThankListAdapter(MyRecevieThank.this,R.layout.thank_list_item,thankList);
                        UpdateGUI();
                    }
                    catch(Exception e){}

                }
            }
        }).start();
    }
    public static void UpdateGUI(){
        handler.post(RefreshLable);
    }
    private static Runnable RefreshLable=new Runnable() {
        @Override
        public void run() {
            listView.setAdapter(thankListAdapter);
//            arrayListAdapter.notifyDataSetChanged();
        }
    };
}
