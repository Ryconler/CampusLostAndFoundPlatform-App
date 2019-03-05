package com.example.dell.campuslostandfoundplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText repassword;
    private EditText tel;
    private EditText qq;
    private Button register;

    private static ProgressBar progress;

    private static int MODE= Context.MODE_PRIVATE;
    private static final String PREFERENCE_NAME="SavaSetting";

    private static Handler handler=new Handler();

    private static int uid;
    private static String usernameData;
    private static String passwordData;
    private static String repasswordData;
    private static String telData;
    private static String qqData;
    private static String schoolData;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView title=(TextView)findViewById(R.id.text_title);
        title.setText("注册");

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        repassword=(EditText)findViewById(R.id.repassword);
        tel=(EditText)findViewById(R.id.tel);
        qq=(EditText)findViewById(R.id.qq);
        register=(Button)findViewById(R.id.register);

        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameData=username.getText().toString();
                passwordData=password.getText().toString();
                repasswordData=repassword.getText().toString();
                telData=tel.getText().toString();
                qqData=qq.getText().toString();

                loadSharedPreferences();

                Pattern patternUsr = Pattern.compile("^[A-Za-z1-9]{4,16}+$");
                Matcher matcherUsr = patternUsr.matcher(usernameData);

                Pattern patternPsw = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
                Matcher matcherPsw = patternPsw.matcher(passwordData);

                Pattern patternNum = Pattern.compile("[0-9]*");
                Matcher mTel=patternNum.matcher(telData);
                Matcher mQq=patternNum.matcher(qqData);

                if(!matcherUsr.matches()){
                    Toast.makeText(RegisterActivity.this,"用户名应为数字和字母，且长度应为4-16个字符",Toast.LENGTH_SHORT).show();
                }
                else if(!matcherPsw.matches()){
                    Toast.makeText(RegisterActivity.this,"密码应为数字和字母的组合，且长度应为6-16个字符",Toast.LENGTH_SHORT).show();
                }
                else if(!passwordData.equals(repasswordData)){
                    Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }
                else if(!mTel.matches()||telData.length()!=11){
                    Toast.makeText(RegisterActivity.this,"请填写11位长度的手机号",Toast.LENGTH_SHORT).show();
                }
                else if (!qqData.equals("")&&(!mQq.matches()||qqData.length()<5||qqData.length()>10)){
                    Toast.makeText(RegisterActivity.this,"qq号应为5-10位的数字",Toast.LENGTH_SHORT).show();
                }
                else {
                    progress.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result=false;
                            try{
                                OkHttpClient client=new OkHttpClient();
                                String url="http://"+GetIP.getIP()+":8080/CampusLostAndFoundPlatform/register.action";
                                FormBody.Builder builder = new FormBody.Builder();
                                builder.add("username",usernameData);
                                builder.add("password",passwordData);
                                builder.add("school",schoolData);
                                builder.add("tel",telData);
                                builder.add("qq",qqData);
                                RequestBody formBody = builder.build();
                                Request request=new Request.Builder()
                                        .url(url)
                                        .post(formBody)
                                        .build();
                                Response response=client.newCall(request).execute();

                                String responseData=response.body().string();
                                result=parseJsonWithJsonObject(responseData);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                if(result){
                                    UpdateGUI();
                                    saveSharedPreferences();
                                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this,"注册成功"+String.valueOf(uid),Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    finish();
                                }
                                else {
                                    UpdateGUI();
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        }

                    }).start();
                }
            }
        });
    }

    private void saveSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putInt("uid",uid);
        editor.putString("username",usernameData);
        editor.putString("password",passwordData);
        editor.putString("school",schoolData);
        editor.putString("nowschool",schoolData);
        editor.putString("qq",qqData);
        editor.putString("tel",telData);
        editor.commit();
    }
    private boolean parseJsonWithJsonObject(String jsonData){
        boolean result=false;
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            result=jsonObject.getBoolean("result");
            uid=jsonObject.getInt("uid");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFERENCE_NAME,MODE);
        schoolData=sharedPreferences.getString("school","");
    }
    public static void UpdateGUI(){
        handler.post(RefreshLable);
    }
    private static Runnable RefreshLable=new Runnable() {
        @Override
        public void run() {
            progress.setVisibility(View.INVISIBLE);
        }
    };
}
