package com.example.dell.campuslostandfoundplatform;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleDetailActivity extends AppCompatActivity {
    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";

    private TextView text_title;
    private Button thankButton;
    private Button foundButton;
    private static TextView title;
    private static TextView articleType;
    private static TextView address;
    private static TextView school;
    private static TextView contactName;
    private static TextView contactTel;
    private static TextView contactQq;
    private static TextView date;
    private static TextView detail;
    private static ImageView hasFound;
    private static TextView username;


    private static Handler handler=new Handler();

    private static String aid;
    private static String title_data;
    private static String articleType_data;
    private static String address_data;
    private static String school_data;
    private static String contactName_data;
    private static String contactTel_data;
    private static String contactQq_data;
    private static String date_data;
    private static String detail_data;
    private static String state_data;
    private static int money_data;
    private static int type_data;

    private static String myUsername;

    private static String username_data;

    private static String sUid;
    private static String rUid;
    private static String thankContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);


        text_title=(TextView)findViewById(R.id.text_title);
        text_title.setText("详细信息");

        thankButton=(Button)findViewById(R.id.thankButton);
        foundButton=(Button)findViewById(R.id.foundButton);
        foundButton.setVisibility(View.GONE);

        title=(TextView)findViewById(R.id.title);
        articleType=(TextView)findViewById(R.id.articleType);
        address=(TextView)findViewById(R.id.address);
        school=(TextView)findViewById(R.id.school);
        contactName=(TextView)findViewById(R.id.contactName);
        contactTel=(TextView)findViewById(R.id.contactTel);
        contactQq=(TextView)findViewById(R.id.contactQq);
        date=(TextView)findViewById(R.id.date);
        detail=(TextView)findViewById(R.id.detail);
        hasFound=(ImageView)findViewById(R.id.hasFound);
        username=(TextView)findViewById(R.id.username);

        loadSharedPreferences();
        Intent intent=getIntent();
        aid=intent.getStringExtra("aid");
        setView();
        //延迟0.1秒，让子线程（获取网络数据））加载完毕后再执行
        try
        {
            Thread.currentThread().sleep(300);//毫秒
            initButton();
            initFoundButton();
        }
        catch(Exception e){}

    }

    private void initFoundButton(){

        if(username_data.equals(myUsername)&&state_data.equals("0")){
            foundButton.setVisibility(View.VISIBLE);
            foundButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText editText=new EditText(ArticleDetailActivity.this);
                    AlertDialog.Builder dialog=new AlertDialog.Builder(ArticleDetailActivity.this);
//                dialog.setTitle("写下你的感谢");
                    dialog.setTitle("确认");
                    dialog.setMessage("确认找回？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confirmFound();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }
            });
        }
    }
    private void confirmFound(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result=false;
                try {
                    OkHttpClient client = new OkHttpClient();
                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/setHasFound.action";
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("aid",aid);
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    result=jsonObject.getBoolean("result");
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(result){
                        Looper.prepare();
                        Toast.makeText(ArticleDetailActivity.this,"确认找回成功",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(ArticleDetailActivity.this,"内部错误",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }
        }).start();
    }
    private void initButton(){
        if (type_data==1){
            thankButton.setText("酬金："+money_data);
            thankButton.setEnabled(false);
        }
        else {
            thankButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myUsername.equals(username_data)){
                        Toast.makeText(ArticleDetailActivity.this,"你不能给自己发送感谢信哦",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final EditText editText=new EditText(ArticleDetailActivity.this);
                        AlertDialog.Builder dialog=new AlertDialog.Builder(ArticleDetailActivity.this);
//                dialog.setTitle("写下你的感谢");
                        dialog.setTitle("To:"+username_data);
                        dialog.setMessage("(感谢信会在首页被其他人看到哦）");
                        dialog.setView(editText);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(editText.getText().toString().equals("")){
                                    Toast.makeText(ArticleDetailActivity.this,"感谢信内容不能为空哦",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    thankContent=editText.getText().toString();
                                    sendThankContent();
                                }
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                }
            });
        }
    }
    private void sendThankContent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result=false;
                try {
                    OkHttpClient client = new OkHttpClient();
                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/addThank.action";
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("aid",aid);
                    builder.add("sUid",sUid);
                    builder.add("rUid",rUid);
                    builder.add("content",thankContent);

                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    result=jsonObject.getBoolean("result");
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(result){
                        Looper.prepare();
                        Toast.makeText(ArticleDetailActivity.this,"发送成功，他/她有机会能在首页中看到哦",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(ArticleDetailActivity.this,"发送失败，请先登录",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }
        }).start();
    }
    private void setView(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/articleDetail.action";
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("aid",aid);
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    JSONArray jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    title_data=jsonObject.getString("title");
                    articleType_data=jsonObject.getString("articleType");
                    address_data=jsonObject.getString("address");
                    contactName_data=jsonObject.getString("contactName");
                    contactQq_data=jsonObject.getString("contactQq");
                    contactTel_data=jsonObject.getString("contactTel");
                    date_data=jsonObject.getString("date");
                    detail_data=jsonObject.getString("detail");
                    school_data=jsonObject.getString("school");
                    state_data=jsonObject.getString("state");
                    username_data=jsonObject.getJSONObject("user").getString("username");
                    rUid=jsonObject.getJSONObject("user").getString("uid");
                    type_data=jsonObject.getInt("type");
                    money_data=jsonObject.getInt("money");

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    UpdateGUI();
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
            title.setText(title_data);
            articleType.setText(articleType_data);
            address.setText(address_data);
            school.setText(school_data);
            contactName.setText(contactName_data);
            contactTel.setText(contactTel_data);
            contactQq.setText(contactQq_data);
            date.setText(date_data);
            detail.setText(detail_data);
            username.setText(username_data);
            if (state_data.equals("0")) {
                hasFound.setImageResource(R.drawable.lost);
            }
            else hasFound.setImageResource(R.drawable.found);

        }
    };

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        sUid=String.valueOf(sharedPreferences.getInt("uid",0));
        myUsername= sharedPreferences.getString("username", "");
    }
    private void clearStaticItem(){
        title_data="";
        articleType_data="";
        address_data="";
        contactName_data="";
        contactQq_data="";
        contactTel_data="";
        date_data="";
        detail_data="";
        school_data="";
        state_data="";
        username_data="";
        rUid="";
        type_data=0;
        money_data=0;
    }
}
