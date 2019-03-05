package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MyInfoActivity extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private TextView school;
    private TextView tel;
    private TextView qq;

    private String usernameData;
    private String qqData;
    private String schoolData;
    private String passwordData;
    private String telData;

    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        TextView title=(TextView)findViewById(R.id.text_title);
        title.setText("我的信息");
        username=(TextView)findViewById(R.id.usernameInfo);
        password=(TextView)findViewById(R.id.passwordInfo);
        tel=(TextView)findViewById(R.id.telInfo);
        school=(TextView)findViewById(R.id.schoolInfo);
        qq=(TextView)findViewById(R.id.qqInfo);

        loadSharedPreferences();
        username.setText(usernameData);
        password.setText(passwordData);
        tel.setText(telData);
        qq.setText(qqData);
        school.setText(schoolData);

    }
    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        usernameData=sharedPreferences.getString("username","");
        passwordData=sharedPreferences.getString("password","");
        qqData=sharedPreferences.getString("qq","");
        telData=sharedPreferences.getString("tel","");
        schoolData=sharedPreferences.getString("school","");


    }

}
