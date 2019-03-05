package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GetSchoolActivity extends AppCompatActivity {

    private Button next;
    private Spinner province;
    private Spinner school;

    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";

    final static String[]pro={"江苏","北京","上海"};
    final static String[]sch={"南通大学","南京大学","苏州大学"
                        ,"北京大学","清华大学","中国人民大学"
                        ,"复旦大学","同济大学","上海交通大学"};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_school);

        TextView title=(TextView)findViewById(R.id.text_title);
        title.setText("选择学校");

        next=(Button)findViewById(R.id.next);
        province=(Spinner)findViewById(R.id.province);
        school=(Spinner)findViewById(R.id.school);

        List<String> listPro=new ArrayList<String>();
        for(int i=0;i<pro.length;i++){
            listPro.add(pro[i]);
        }
        ArrayAdapter<String>adapterPro=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listPro);
        adapterPro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapterPro);

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSchoolSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setSchoolSpinner();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("ACTION").equals("register")){
                    Intent intent=new Intent(GetSchoolActivity.this,RegisterActivity.class);
                    saveSharedPreferences1();
                    startActivity(intent);
                }
                if(getIntent().getStringExtra("ACTION").equals("casual")){
                    Intent intent=new Intent(GetSchoolActivity.this,MainActivity.class);
                    saveSharedPreferences2();
                    startActivity(intent);
                }
                if(getIntent().getStringExtra("ACTION").equals("changeNowSchool")){
                    Intent intent=new Intent(GetSchoolActivity.this,MainActivity.class);
                    saveSharedPreferences2();
                    startActivity(intent);
                    finish();
                }

            }

        });

    }
    private void setSchoolSpinner(){
        String getPro=province.getSelectedItem().toString();
        List<String>list=new ArrayList<String>();
        list.clear();
        if(getPro.equals("江苏")){
            for(int i=0;i<3;i++){
                list.add(sch[i]);
            }
        }
        if(getPro.equals("北京")){
            for(int i=3;i<6;i++){
                list.add(sch[i]);
            }
        }
        if(getPro.equals("上海")){
            for(int i=6;i<9;i++){
                list.add(sch[i]);
            }
        }
        ArrayAdapter<String>adapterSch=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapterSch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school.setAdapter(adapterSch);
    }

    private void saveSharedPreferences1(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("school",school.getSelectedItem().toString());
        editor.putString("nowschool",school.getSelectedItem().toString());
        editor.commit();
    }
    private void saveSharedPreferences2(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("nowschool",school.getSelectedItem().toString());
        editor.commit();
    }

}
