package com.example.dell.campuslostandfoundplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView title=(TextView)findViewById(R.id.text_title);
        title.setText("小组成员");
    }
}
