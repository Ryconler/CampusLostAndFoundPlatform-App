package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishFoundActivity extends AppCompatActivity {
    private TextView text_title;
    private EditText titleEnter;
    private EditText addressEnter;
    private EditText detailEnter;
    private EditText nameEnter;
    private EditText telEnter;
    private EditText qqEnter;
    private Button publishLost;
    private Spinner provinceSelect;
    private Spinner schoolSelect;
    private Spinner articleTypeSelect;
    private EditText moneyEnter;

    private Spinner timeSpinner;

    private static String title;
    private static String address;
    private static String detail;
    private static String name;
    private static String tel;
    private static String qq;
    private static String articleType;

    private static String school;
    private static String money;

    private static String username;

    final static String[]pro={"江苏","北京","上海"};
    final static String[]sch={"南通大学","南京大学","苏州大学"
            ,"北京大学","清华大学","中国人民大学"
            ,"复旦大学","同济大学","上海交通大学"};
    final static String[]atype={"证件","钱包","宠物","钥匙","书本","电子产品","其他"};

    public static int MODE= Context.MODE_PRIVATE;
    public static final String PREFERENCE_NAME="SavaSetting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_found);
        loadSharedPreferences();
        isLogin();

        text_title=(TextView)findViewById(R.id.text_title);
        text_title.setText("填写寻物启事信息");

        titleEnter=(EditText)findViewById(R.id.titleEnter);
        addressEnter=(EditText)findViewById(R.id.addressEnter);
        detailEnter=(EditText)findViewById(R.id.detailEnter);
        nameEnter=(EditText)findViewById(R.id.nameEnter);
        telEnter=(EditText)findViewById(R.id.telEnter);
        qqEnter=(EditText)findViewById(R.id.qqEnter);
        publishLost=(Button)findViewById(R.id.publishLost);
        provinceSelect=(Spinner)findViewById(R.id.provinceSelect);
        schoolSelect=(Spinner)findViewById(R.id.schoolSelect);
        articleTypeSelect=(Spinner)findViewById(R.id.articleTypeSelect);
        moneyEnter=(EditText) findViewById(R.id.money);

        List<String> listPro=new ArrayList<String>();
        List<String>listAType=new ArrayList<String >();
        for(int i=0;i<pro.length;i++){
            listPro.add(pro[i]);
        }
        for(int i=0;i<atype.length;i++){
            listAType.add(atype[i]);
        }
        ArrayAdapter<String> adapterPro=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listPro);
        ArrayAdapter<String> adapterAType=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listAType);
        adapterPro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterAType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSelect.setAdapter(adapterPro);
        articleTypeSelect.setAdapter(adapterAType);
        //省份选择选项的监听器
        provinceSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSchoolSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setSchoolSpinner();
            }
        });
        //发布按钮监听器
        initPublishButton();

    }
    private void isLogin(){
        if(username.equals("")){
            Intent intent=new Intent(this,LoginOrRegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
        }
    }
    private void initPublishButton(){
        publishLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=titleEnter.getText().toString();
                address=addressEnter.getText().toString();
                detail=detailEnter.getText().toString();
                name=nameEnter.getText().toString();
                tel=telEnter.getText().toString();
                qq=qqEnter.getText().toString();
                articleType=articleTypeSelect.getSelectedItem().toString();
                school=schoolSelect.getSelectedItem().toString();
                money=moneyEnter.getText().toString();

                Pattern patternNum = Pattern.compile("[0-9]*");
                Matcher mTel=patternNum.matcher(tel);
                Matcher mQq=patternNum.matcher(qq);
                Matcher mMoney=patternNum.matcher(money);
                if(title.length()<3||title.length()>20){
                    Toast.makeText(PublishFoundActivity.this,"标题应在3到20个字之间",Toast.LENGTH_SHORT).show();
                }
                else if(address.length()<2||address.length()>20){
                    Toast.makeText(PublishFoundActivity.this,"地拾物地点应在2到20个字之间",Toast.LENGTH_SHORT).show();
                }
                else if(name.equals("")||name.length()>10){
                    Toast.makeText(PublishFoundActivity.this,"联系人姓名不能为空且字数不超过10个字",Toast.LENGTH_SHORT).show();
                }
                else if(!detail.equals("")&&(detail.length()>100)){
                    Toast.makeText(PublishFoundActivity.this,"详细描述不能超过100个字",Toast.LENGTH_SHORT).show();
                }
                else if(!mTel.matches()||tel.length()!=11){
                    Toast.makeText(PublishFoundActivity.this,"手机号应为11位的数字",Toast.LENGTH_SHORT).show();
                }
                else if (!qq.equals("")&&(!mQq.matches()||qq.length()<5||qq.length()>10)){
                    Toast.makeText(PublishFoundActivity.this,"qq号应为5-10位的数字",Toast.LENGTH_SHORT).show();
                }
                else if(!money.equals("")&&(!mMoney.matches()||money.length()>5)){
                    Toast.makeText(PublishFoundActivity.this,"酬金应为五位数以下的数字",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result=false;
                            try{
                                OkHttpClient client=new OkHttpClient();

                                String url="http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/addArticle.action";

                                FormBody.Builder builder = new FormBody.Builder();
                                builder.add("type","1");
                                builder.add("username",username);
                                builder.add("title",title);
                                builder.add("address",address);
                                builder.add("detail",detail);
                                builder.add("contactName",name);
                                builder.add("contactTel",tel);
                                builder.add("qq",qq);
                                builder.add("articleType",articleType);
                                builder.add("school",school);
                                if(money.equals("")){
                                    builder.add("money","0");
                                }
                                else builder.add("money",money);
                                RequestBody formBody = builder.build();

                                Request request=new Request.Builder()
                                        .url(url)
                                        .post(formBody)
                                        .build();
                                Response response=client.newCall(request).execute();
                                String responseData=response.body().string();
                                JSONArray jsonArray=new JSONArray(responseData);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                result=jsonObject.getBoolean("result");
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                if(result){
                                    finish();
                                    Looper.prepare();
                                    Toast.makeText(PublishFoundActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(PublishFoundActivity.this,"内部错误",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }

                        }
                    }).start();
                }
            }
        });
    }

    //根据选择的省市来确定学校
    public void setSchoolSpinner(){
        String getPro=provinceSelect.getSelectedItem().toString();
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
        schoolSelect.setAdapter(adapterSch);

    }
    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        username=sharedPreferences.getString("username","");
    }
}
